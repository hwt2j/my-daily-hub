package com.mydailyhub.backend.todo.manage.service;

import com.mydailyhub.backend.common.base.exception.BusinessException;
import com.mydailyhub.backend.common.base.exception.ErrorCode;
import com.mydailyhub.backend.todo.manage.dto.*;
import com.mydailyhub.backend.todo.manage.entity.*;
import com.mydailyhub.backend.todo.manage.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TodoMainServiceTests {
    private TodoMainRepository mainRepository;
    private TodoRoutineRepository routineRepository;
    private TodoRoutineDetailRepository detailRepository;
    private TodoMainService service;

    @BeforeEach
    void setUp() {
        mainRepository = mock(TodoMainRepository.class);
        routineRepository = mock(TodoRoutineRepository.class);
        detailRepository = mock(TodoRoutineDetailRepository.class);
        service = new TodoMainService(mainRepository, routineRepository, detailRepository);
    }

    @ParameterizedTest
    @CsvSource({"WEEKLY,0", "WEEKLY,8", "MONTHLY,0", "MONTHLY,32"})
    void rejectsOutOfRangeBeforeSaving(String period, int value) {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.create(createRequest(settings(period, value))));
        assertEquals(ErrorCode.INVALID_INPUT, exception.getErrorCode());
        verifyNoInteractions(mainRepository, routineRepository, detailRepository);
    }

    @Test
    void rejectsMissingEmptyDuplicateAndUnsupportedSettings() {
        List<TodoRoutineRequest> invalid = List.of(
                settings("WEEKLY"), settings("WEEKLY", 1, 1),
                settings("MONTHLY", 31, 31),
                new TodoRoutineRequest("WEEKLY", LocalDate.now(), null, "DAY_OF_WEEK", null),
                new TodoRoutineRequest("WEEKLY", LocalDate.now(), null, "DAY_OF_WEEK",
                        List.of(new TodoRoutineDetailRequest(null))));
        for (TodoRoutineRequest settings : invalid) {
            assertThrows(BusinessException.class, () -> service.create(createRequest(settings)));
        }
        assertThrows(BusinessException.class, () -> service.create(createRequest(null)));
        assertThrows(BusinessException.class, () -> service.create(new TodoMainCreateRequest(
                "test", false, null, null, null, settings("WEEKLY", 1))));
        verifyNoInteractions(mainRepository, routineRepository, detailRepository);
    }

    @ParameterizedTest
    @CsvSource({"WEEKLY,1", "WEEKLY,7", "MONTHLY,1", "MONTHLY,31"})
    void createsRoutineAndReturnsNestedResponse(String period, int value) {
        when(mainRepository.save(any())).thenAnswer(invocation -> {
            TodoMain todo = invocation.getArgument(0);
            todo.setTdSeq(1L);
            return todo;
        });
        when(routineRepository.save(any())).thenAnswer(invocation -> {
            TodoRoutine routine = invocation.getArgument(0);
            routine.setRtSeq(10L);
            when(routineRepository.findByTodoId(1L)).thenReturn(Optional.of(routine));
            when(routineRepository.findActiveByTodoId(1L)).thenReturn(Optional.of(routine));
            return routine;
        });
        when(detailRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<TodoRoutineDetail> details = invocation.getArgument(0);
            when(detailRepository.findActiveByRoutineId(10L))
                    .thenReturn(details);
            return details;
        });

        TodoMainResponse response = service.create(createRequest(settings(period, value)));

        assertTrue(response.routine());
        assertEquals("MORNING", response.routineSettings().rtTimePeriodCode());
        assertEquals(1, response.routineSettings().details().size());
        assertEquals("WEEKLY".equals(period) ? "DAY_OF_WEEK" : "DAY_OF_MONTH",
                response.routineSettings().details().getFirst().rtDetailType());
        assertEquals(String.valueOf(value), response.routineSettings().details().getFirst().rtDetailValue());
    }

    @Test
    void updatesExistingRoutineAndReplacesActiveDetails() {
        TodoMain todo = todo(1L);
        TodoRoutine routine = routine(todo);
        TodoRoutineDetail old = new TodoRoutineDetail();
        when(mainRepository.findByTdSeqAndDeletedFalse(1L)).thenReturn(Optional.of(todo));
        when(routineRepository.findByTodoId(1L)).thenReturn(Optional.of(routine));
        when(detailRepository.findActiveByRoutineId(10L))
                .thenReturn(List.of(old));

        service.update(1L, new TodoMainUpdateRequest("changed", true, null, null, null,
                settings("MONTHLY", 1, 31)));

        verify(routineRepository).save(same(routine));
        assertTrue(old.isDeleted());
        assertEquals("MONTHLY", routine.getRtRepeatedCycleCode());
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<TodoRoutineDetail>> captor = ArgumentCaptor.forClass(List.class);
        verify(detailRepository).saveAll(captor.capture());
        assertEquals(List.of("1", "31"), captor.getValue().stream()
                .map(TodoRoutineDetail::getRtDetailValue).toList());
        assertTrue(captor.getValue().stream().allMatch(detail -> detail.getTodoRoutine() == routine));
    }

    @Test
    void disablingThenEnablingReusesRoutineRow() {
        TodoMain todo = todo(1L);
        TodoRoutine routine = routine(todo);
        TodoRoutineDetail detail = new TodoRoutineDetail();
        when(mainRepository.findByTdSeqAndDeletedFalse(1L)).thenReturn(Optional.of(todo));
        when(routineRepository.findByTodoId(1L)).thenReturn(Optional.of(routine));
        when(detailRepository.findActiveByRoutineId(10L))
                .thenReturn(List.of(detail));

        TodoMainResponse disabled = service.update(1L,
                new TodoMainUpdateRequest("test", false, null, null, null, null));
        assertTrue(routine.isDeleted());
        assertTrue(detail.isDeleted());
        assertNull(disabled.routineSettings());

        service.update(1L, new TodoMainUpdateRequest("test", true, null, null, null,
                settings("WEEKLY", 7)));
        assertFalse(routine.isDeleted());
        assertEquals(10L, routine.getRtSeq());
        verify(routineRepository).save(same(routine));
    }

    @Test
    void deletingTodoAlsoSoftDeletesRoutineAndDetails() {
        TodoMain todo = todo(1L);
        TodoRoutine routine = routine(todo);
        TodoRoutineDetail detail = new TodoRoutineDetail();
        when(mainRepository.findByTdSeqAndDeletedFalse(1L)).thenReturn(Optional.of(todo));
        when(routineRepository.findByTodoId(1L)).thenReturn(Optional.of(routine));
        when(detailRepository.findActiveByRoutineId(10L))
                .thenReturn(List.of(detail));
        service.delete(1L);
        assertTrue(todo.isDeleted());
        assertTrue(routine.isDeleted());
        assertTrue(detail.isDeleted());
    }

    @Test
    void listsNestedChildrenUsingBatchQueries() {
        TodoMain first = todo(1L);
        TodoMain second = todo(2L);
        TodoRoutine routine = routine(first);
        TodoRoutineDetail detail = new TodoRoutineDetail();
        detail.setTodoRoutine(routine);
        detail.setRtDetailType("DAY_OF_WEEK");
        detail.setRtDetailValue("1");
        when(mainRepository.findAllByDeletedFalse(any())).thenReturn(List.of(first, second));
        when(routineRepository.findActiveByTodoIds(List.of(1L, 2L)))
                .thenReturn(List.of(routine));
        when(detailRepository.findActiveByRoutineIds(List.of(10L)))
                .thenReturn(List.of(detail));

        List<TodoMainResponse> responses = service.findAll();
        assertEquals("1", responses.getFirst().routineSettings().details().getFirst().rtDetailValue());
        assertNull(responses.get(1).routineSettings());
        verify(routineRepository, times(1)).findActiveByTodoIds(anyCollection());
        verify(detailRepository, times(1))
                .findActiveByRoutineIds(anyCollection());
        verifyNoMoreInteractions(routineRepository, detailRepository);
    }

    @Test
    void findsSingleTodoWithRoutineWithoutBatchQueries() {
        TodoMain todo = todo(1L);
        TodoRoutine routine = routine(todo);
        TodoRoutineDetail detail = new TodoRoutineDetail();
        detail.setRtDetailValue("7");
        when(mainRepository.findByTdSeqAndDeletedFalse(1L)).thenReturn(Optional.of(todo));
        when(routineRepository.findActiveByTodoId(1L)).thenReturn(Optional.of(routine));
        when(detailRepository.findActiveByRoutineId(10L))
                .thenReturn(List.of(detail));

        TodoMainResponse response = service.findById(1L);

        assertEquals(1L, response.tdSeq());
        assertEquals("7", response.routineSettings().details().getFirst().rtDetailValue());
        verify(routineRepository).findActiveByTodoId(1L);
        verify(detailRepository).findActiveByRoutineId(10L);
        verifyNoMoreInteractions(routineRepository, detailRepository);
    }

    @Test
    void nonRoutineTodoDoesNotQueryChildren() {
        TodoMain todo = todo(1L);
        todo.setRoutine(false);
        when(mainRepository.findByTdSeqAndDeletedFalse(1L)).thenReturn(Optional.of(todo));

        assertNull(service.findById(1L).routineSettings());
        verifyNoInteractions(routineRepository, detailRepository);
    }

    @Test
    void adminSingleResponseIncludesDeletedRoutineAndDetails() {
        TodoMain todo = todo(1L);
        todo.setDeleted(true);
        TodoRoutine routine = routine(todo);
        routine.setDeleted(true);
        when(mainRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(routineRepository.findByTodoId(1L)).thenReturn(Optional.of(routine));
        TodoRoutineDetail detail = new TodoRoutineDetail();
        detail.setDeleted(true);
        when(detailRepository.findByRoutineId(10L)).thenReturn(List.of(detail));

        TodoMainResponse response = service.findByIdForAdmin(1L);

        assertTrue(response.deleted());
        assertTrue(response.routineSettings().deleted());
        assertTrue(response.routineSettings().details().getFirst().deleted());
        verify(routineRepository).findByTodoId(1L);
        verify(detailRepository).findByRoutineId(10L);
        verifyNoMoreInteractions(routineRepository, detailRepository);
    }

    @Test
    void adminListIncludesDeletedChildrenOnlyForRoutineTodos() {
        TodoMain enabled = todo(1L);
        enabled.setDeleted(true);
        TodoMain disabled = todo(2L);
        disabled.setRoutine(false);
        TodoRoutine routine = routine(enabled);
        routine.setDeleted(true);
        TodoRoutineDetail deleted = new TodoRoutineDetail();
        deleted.setTodoRoutine(routine);
        deleted.setDeleted(true);
        TodoRoutineDetail active = new TodoRoutineDetail();
        active.setTodoRoutine(routine);
        when(mainRepository.findAll(any(org.springframework.data.domain.Sort.class)))
                .thenReturn(List.of(enabled, disabled));
        when(routineRepository.findByTodoIds(List.of(1L))).thenReturn(List.of(routine));
        when(detailRepository.findByRoutineIds(List.of(10L))).thenReturn(List.of(deleted, active));

        List<TodoMainResponse> responses = service.findAllForAdmin();

        assertTrue(responses.getFirst().deleted());
        assertTrue(responses.getFirst().routineSettings().deleted());
        assertEquals(2, responses.getFirst().routineSettings().details().size());
        assertTrue(responses.getFirst().routineSettings().details().getFirst().deleted());
        assertFalse(responses.getFirst().routineSettings().details().get(1).deleted());
        assertNull(responses.get(1).routineSettings());
        verify(routineRepository).findByTodoIds(List.of(1L));
        verify(detailRepository).findByRoutineIds(List.of(10L));
        verifyNoMoreInteractions(routineRepository, detailRepository);
    }

    @Test
    void adminNonRoutineSingleAndListDoNotQueryChildren() {
        TodoMain todo = todo(1L);
        todo.setRoutine(false);
        todo.setDeleted(true);
        when(mainRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(mainRepository.findAll(any(org.springframework.data.domain.Sort.class)))
                .thenReturn(List.of(todo));

        assertNull(service.findByIdForAdmin(1L).routineSettings());
        assertNull(service.findAllForAdmin().getFirst().routineSettings());
        verifyNoInteractions(routineRepository, detailRepository);
    }

    @Test
    void regularLookupDoesNotFallBackToDeletedRoutine() {
        when(mainRepository.findByTdSeqAndDeletedFalse(1L)).thenReturn(Optional.of(todo(1L)));
        when(routineRepository.findActiveByTodoId(1L)).thenReturn(Optional.empty());

        assertNull(service.findById(1L).routineSettings());
        verify(routineRepository).findActiveByTodoId(1L);
        verifyNoMoreInteractions(routineRepository);
        verifyNoInteractions(detailRepository);
    }

    @Test
    void otherCyclesIgnoreMissingEmptyAndInvalidDetails() {
        List<List<TodoRoutineDetailRequest>> inputs = java.util.Arrays.asList(
                null, List.of(), java.util.Arrays.asList(null, new TodoRoutineDetailRequest(null),
                        new TodoRoutineDetailRequest(99), new TodoRoutineDetailRequest(99)));
        for (var details : inputs) {
            service.create(createRequest(new TodoRoutineRequest(
                    "DAILY", LocalDate.of(2026, 9, 18), null, "MORNING", details)));
        }
        verify(routineRepository, times(3)).save(any());
        verifyNoInteractions(detailRepository);
    }

    @Test
    void changingToOtherCycleDeactivatesOldDetailsWithoutCreatingNewOnes() {
        TodoMain todo = todo(1L);
        TodoRoutine routine = routine(todo);
        routine.setRtRepeatedCycleCode("WEEKLY");
        TodoRoutineDetail old = new TodoRoutineDetail();
        when(mainRepository.findByTdSeqAndDeletedFalse(1L)).thenReturn(Optional.of(todo));
        when(routineRepository.findByTodoId(1L)).thenReturn(Optional.of(routine));
        when(detailRepository.findActiveByRoutineId(10L)).thenReturn(List.of(old));

        service.update(1L, new TodoMainUpdateRequest("test", true, null, null, null,
                new TodoRoutineRequest("DAILY", LocalDate.of(2026, 9, 18), null, "MORNING", null)));

        assertTrue(old.isDeleted());
        assertEquals("DAILY", routine.getRtRepeatedCycleCode());
        verify(detailRepository, never()).saveAll(any());
    }

    @ParameterizedTest
    @org.junit.jupiter.params.provider.ValueSource(strings = {"WEEKLY", "MONTHLY"})
    void detailCyclesRejectNullEntriesAndMissingValues(String cycle) {
        List<List<TodoRoutineDetailRequest>> inputs = java.util.Arrays.asList(
                null, List.of(), java.util.Arrays.asList((TodoRoutineDetailRequest) null),
                List.of(new TodoRoutineDetailRequest(null)));
        for (var details : inputs) {
            assertThrows(BusinessException.class, () -> service.create(createRequest(
                    new TodoRoutineRequest(cycle, LocalDate.of(2026, 9, 18), null, "MORNING", details))));
        }
        verifyNoInteractions(mainRepository, routineRepository, detailRepository);
    }

    @Test
    void requestValidationAllowsOtherCyclesWithoutDetailsAndKeepsTimePeriodRequired() {
        try (var factory = jakarta.validation.Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            assertTrue(validator.validate(createRequest(new TodoRoutineRequest(
                    "DAILY", LocalDate.of(2026, 9, 18), null, "MORNING", null))).isEmpty());
            assertTrue(validator.validate(createRequest(new TodoRoutineRequest(
                    "DAILY", LocalDate.of(2026, 9, 18), null, "MORNING",
                    java.util.Arrays.asList(null, new TodoRoutineDetailRequest(null))))).isEmpty());
            assertFalse(validator.validate(createRequest(new TodoRoutineRequest(
                    "DAILY", LocalDate.of(2026, 9, 18), null, null, null))).isEmpty());
        }
    }

    private TodoMain todo(Long id) {
        TodoMain todo = new TodoMain();
        todo.setTdSeq(id);
        todo.setRoutine(true);
        return todo;
    }

    private TodoRoutine routine(TodoMain todo) {
        TodoRoutine routine = new TodoRoutine();
        routine.setRtSeq(10L);
        routine.setTodoMain(todo);
        return routine;
    }

    private TodoMainCreateRequest createRequest(TodoRoutineRequest settings) {
        return new TodoMainCreateRequest("test", true, null, null, null, settings);
    }

    private TodoRoutineRequest settings(String period, Integer... values) {
        return new TodoRoutineRequest(period, LocalDate.of(2026, 9, 18), null, "MORNING",
                java.util.Arrays.stream(values).map(TodoRoutineDetailRequest::new).toList());
    }
}
