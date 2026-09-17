package com.mydailyhub.backend.todo.manage.service;

import com.mydailyhub.backend.todo.manage.dto.TodoMainCreateRequest;
import com.mydailyhub.backend.todo.manage.dto.TodoMainResponse;
import com.mydailyhub.backend.todo.manage.dto.TodoMainUpdateRequest;
import com.mydailyhub.backend.todo.manage.entity.TodoMain;
import com.mydailyhub.backend.todo.manage.repository.TodoMainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoMainService {

    private final TodoMainRepository todoMainRepository;

    @Transactional
    public TodoMainResponse create(TodoMainCreateRequest request) {
        TodoMain todo = new TodoMain();
        todo.setTdName(request.tdName());
        todo.setRoutine(request.routine());
        todo.setTdImportance(request.tdImportance());
        todo.setTdSortSn(request.tdSortSn());
        todo.setTdDueDt(request.tdDueDt());
        return TodoMainResponse.from(todoMainRepository.save(todo));
    }

    @Transactional
    public TodoMainResponse update(Long tdSeq, TodoMainUpdateRequest request) {
        TodoMain todo = findActiveTodo(tdSeq);
        todo.setTdName(request.tdName());
        todo.setTdImportance(request.tdImportance());
        todo.setTdSortSn(request.tdSortSn());
        todo.setTdDueDt(request.tdDueDt());
        todo.setRoutine(request.routine());
        todoMainRepository.flush();
        return TodoMainResponse.from(todo);
    }

    @Transactional
    public void delete(Long tdSeq) {
        TodoMain todo = findActiveTodo(tdSeq);
        todo.setDeleted(true);
    }

    public TodoMainResponse findById(Long tdSeq) {
        return TodoMainResponse.from(findActiveTodo(tdSeq));
    }

    private TodoMain findActiveTodo(Long tdSeq) {
        return todoMainRepository.findByTdSeqAndDeletedFalse(tdSeq)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Todo not found: " + tdSeq));
    }

    public List<TodoMainResponse> findAll() {
        return todoMainRepository.findAllByDeletedFalse(Sort.by(Sort.Direction.DESC, "tdSeq"))
                .stream()
                .map(TodoMainResponse::from)
                .toList();
    }

    public TodoMainResponse findByIdForAdmin(Long tdSeq) {
        TodoMain todo = todoMainRepository.findById(tdSeq)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Todo not found: " + tdSeq));
        return TodoMainResponse.from(todo);
    }

    public List<TodoMainResponse> findAllForAdmin() {
        return todoMainRepository.findAll(Sort.by(Sort.Direction.DESC, "tdSeq"))
                .stream()
                .map(TodoMainResponse::from)
                .toList();
    }
}
