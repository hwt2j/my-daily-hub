package com.mydailyhub.backend.todo.manage.service;

import com.mydailyhub.backend.common.base.exception.BusinessException;
import com.mydailyhub.backend.common.base.exception.ErrorCode;
import com.mydailyhub.backend.todo.manage.dto.TodoMainCreateRequest;
import com.mydailyhub.backend.todo.manage.dto.TodoMainResponse;
import com.mydailyhub.backend.todo.manage.dto.TodoMainUpdateRequest;
import com.mydailyhub.backend.todo.manage.dto.TodoRoutineRequest;
import com.mydailyhub.backend.todo.manage.dto.TodoRoutineResponse;
import com.mydailyhub.backend.todo.manage.entity.TodoMain;
import com.mydailyhub.backend.todo.manage.entity.TodoRoutine;
import com.mydailyhub.backend.todo.manage.entity.TodoRoutineDetail;
import com.mydailyhub.backend.todo.manage.repository.TodoMainRepository;
import com.mydailyhub.backend.todo.manage.repository.TodoRoutineDetailRepository;
import com.mydailyhub.backend.todo.manage.repository.TodoRoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoMainService {

    private final TodoMainRepository todoMainRepository;
    private final TodoRoutineRepository todoRoutineRepository;
    private final TodoRoutineDetailRepository todoRoutineDetailRepository;

    @Transactional
    public TodoMainResponse create(TodoMainCreateRequest request) {
        validateRoutine(request.routine(), request.routineSettings());
        TodoMain todo = new TodoMain();
        todo.setTdName(request.tdName());
        todo.setRoutine(request.routine());
        todo.setTdImportance(request.tdImportance());
        todo.setTdSortSn(request.tdSortSn());
        todo.setTdDueDt(request.tdDueDt());
        todoMainRepository.save(todo);
        saveRoutine(todo, request.routineSettings());
        todoMainRepository.flush();
        return toResponse(todo);
    }

    @Transactional
    public TodoMainResponse update(Long tdSeq, TodoMainUpdateRequest request) {
        TodoMain todo = findActiveTodo(tdSeq);
        validateRoutine(request.routine(), request.routineSettings());
        todo.setTdName(request.tdName());
        todo.setTdImportance(request.tdImportance());
        todo.setTdSortSn(request.tdSortSn());
        todo.setTdDueDt(request.tdDueDt());
        todo.setRoutine(request.routine());
        saveRoutine(todo, request.routineSettings());
        todoMainRepository.flush();
        return toResponse(todo);
    }

    @Transactional
    public void delete(Long tdSeq) {
        TodoMain todo = findActiveTodo(tdSeq);
        todo.setDeleted(true);
        deactivateRoutine(tdSeq);
    }

    public TodoMainResponse findById(Long tdSeq) {
        TodoMain todo = findActiveTodo(tdSeq);
        return toResponse(todo);
    }

    private TodoMain findActiveTodo(Long tdSeq) {
        return todoMainRepository.findByTdSeqAndDeletedFalse(tdSeq)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Todo not found: " + tdSeq));
    }

    public List<TodoMainResponse> findAll() {
        return toResponses(todoMainRepository.findAllByDeletedFalse(
                Sort.by(Sort.Direction.DESC, "tdSeq")));
    }

    public TodoMainResponse findByIdForAdmin(Long tdSeq) {
        TodoMain todo = todoMainRepository.findById(tdSeq)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Todo not found: " + tdSeq));
        return toResponse(todo, true);
    }

    public List<TodoMainResponse> findAllForAdmin() {
        return toResponses(todoMainRepository.findAll(Sort.by(Sort.Direction.DESC, "tdSeq")), true);
    }

    private void validateRoutine(boolean enabled, TodoRoutineRequest settings) {
        if (!enabled) {
            if (settings != null) {
                throw invalidRoutine("루틴이 아닌 할 일에는 루틴 설정을 입력할 수 없습니다.");
            }
            return;
        }
        if (settings == null) {
            throw invalidRoutine("루틴 설정은 필수입니다.");
        }
        if (settings.rtRepeatedCycleCode() == null || settings.rtRepeatedCycleCode().isBlank()
                || settings.rtRepeatedCycleCode().length() > 20 || settings.rtStartDt() == null) {
            throw invalidRoutine("반복 주기 코드와 시작일을 올바르게 입력해 주세요.");
        }
        if (settings.rtEndDt() != null && settings.rtEndDt().isBefore(settings.rtStartDt())) {
            throw invalidRoutine("루틴 종료일은 시작일보다 빠를 수 없습니다.");
        }
        if (settings.rtTimePeriodCode() == null || settings.rtTimePeriodCode().isBlank()
                || settings.rtTimePeriodCode().length() > 20) {
            throw invalidRoutine("시간대 코드를 올바르게 입력해 주세요.");
        }
        int maximum = switch (settings.rtRepeatedCycleCode()) {
            case "WEEKLY" -> 7;
            case "MONTHLY" -> 31;
            default -> 0;
        };
        if (maximum == 0) {
            return;
        }
        if (settings.details() == null || settings.details().isEmpty()) {
            throw invalidRoutine("루틴 상세는 1개 이상 입력해 주세요.");
        }
        var values = new HashSet<Integer>();
        for (var detail : settings.details()) {
            if (detail == null || detail.rtDetailValue() == null
                    || detail.rtDetailValue() < 1 || detail.rtDetailValue() > maximum) {
                throw invalidRoutine("루틴 상세 값은 1~" + maximum + " 사이의 정수여야 합니다.");
            }
            if (!values.add(detail.rtDetailValue())) {
                throw invalidRoutine("루틴 상세 값은 중복될 수 없습니다.");
            }
        }
    }

    private BusinessException invalidRoutine(String message) {
        return new BusinessException(ErrorCode.INVALID_INPUT, message);
    }

    private void saveRoutine(TodoMain todo, TodoRoutineRequest settings) {
        if (!todo.isRoutine()) {
            deactivateRoutine(todo.getTdSeq());
            return;
        }
        TodoRoutine routine = todoRoutineRepository.findByTodoId(todo.getTdSeq())
                .orElseGet(TodoRoutine::new);
        routine.setTodoMain(todo);
        routine.setDeleted(false);
        routine.setRtRepeatedCycleCode(settings.rtRepeatedCycleCode());
        routine.setRtStartDt(settings.rtStartDt());
        routine.setRtEndDt(settings.rtEndDt());
        routine.setRtTimePeriodCode(settings.rtTimePeriodCode());
        if (routine.getRtSeq() != null) {
            deactivateDetails(routine.getRtSeq());
        }
        todoRoutineRepository.save(routine);
        String detailType = switch (settings.rtRepeatedCycleCode()) {
            case "WEEKLY" -> "DAY_OF_WEEK";
            case "MONTHLY" -> "DAY_OF_MONTH";
            default -> null;
        };
        if (detailType == null) {
            return;
        }
        List<TodoRoutineDetail> details = settings.details().stream().map(request -> {
            TodoRoutineDetail detail = new TodoRoutineDetail();
            detail.setTodoRoutine(routine);
            detail.setRtDetailType(detailType);
            detail.setRtDetailValue(request.rtDetailValue().toString());
            return detail;
        }).toList();
        todoRoutineDetailRepository.saveAll(details);
    }

    private void deactivateRoutine(Long tdSeq) {
        todoRoutineRepository.findByTodoId(tdSeq).ifPresent(routine -> {
            routine.setDeleted(true);
            deactivateDetails(routine.getRtSeq());
        });
    }

    private void deactivateDetails(Long rtSeq) {
        todoRoutineDetailRepository.findActiveByRoutineId(rtSeq)
                .forEach(detail -> detail.setDeleted(true));
    }

    private TodoMainResponse toResponse(TodoMain todo) {
        return toResponse(todo, false);
    }

    private TodoMainResponse toResponse(TodoMain todo, boolean includeDeleted) {
        if (!todo.isRoutine()) {
            return TodoMainResponse.from(todo, null);
        }
        var routine = includeDeleted
                ? todoRoutineRepository.findByTodoId(todo.getTdSeq())
                : todoRoutineRepository.findActiveByTodoId(todo.getTdSeq());
        TodoRoutineResponse settings = routine
                .map(value -> TodoRoutineResponse.from(value, includeDeleted
                        ? todoRoutineDetailRepository.findByRoutineId(value.getRtSeq())
                        : todoRoutineDetailRepository.findActiveByRoutineId(value.getRtSeq())))
                .orElse(null);
        return TodoMainResponse.from(todo, settings);
    }

    // Load children in batches so list queries do not issue queries for each todo.
    private List<TodoMainResponse> toResponses(List<TodoMain> todos) {
        return toResponses(todos, false);
    }

    private List<TodoMainResponse> toResponses(List<TodoMain> todos, boolean includeDeleted) {
        if (todos.isEmpty()) {
            return List.of();
        }
        List<Long> tdSeqs = todos.stream().filter(TodoMain::isRoutine)
                .map(TodoMain::getTdSeq).toList();
        List<TodoRoutine> routines = tdSeqs.isEmpty() ? List.of()
                : includeDeleted ? todoRoutineRepository.findByTodoIds(tdSeqs)
                : todoRoutineRepository.findActiveByTodoIds(tdSeqs);
        Map<Long, TodoRoutine> routineByTodo = routines.stream().collect(Collectors.toMap(
                routine -> routine.getTodoMain().getTdSeq(), Function.identity()));
        List<Long> rtSeqs = routines.stream().map(TodoRoutine::getRtSeq).toList();
        List<TodoRoutineDetail> details = rtSeqs.isEmpty() ? List.of()
                : includeDeleted ? todoRoutineDetailRepository.findByRoutineIds(rtSeqs)
                : todoRoutineDetailRepository.findActiveByRoutineIds(rtSeqs);
        Map<Long, List<TodoRoutineDetail>> detailsByRoutine = details.stream()
                .collect(Collectors.groupingBy(detail -> detail.getTodoRoutine().getRtSeq()));
        return todos.stream().map(todo -> {
            TodoRoutine routine = routineByTodo.get(todo.getTdSeq());
            TodoRoutineResponse settings = routine == null ? null : TodoRoutineResponse.from(
                    routine, detailsByRoutine.getOrDefault(routine.getRtSeq(), List.of()));
            return TodoMainResponse.from(todo, settings);
        }).toList();
    }
}
