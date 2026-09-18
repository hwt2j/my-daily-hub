package com.mydailyhub.backend.todo.manage.controller;

import com.mydailyhub.backend.common.base.response.ApiResponse;
import com.mydailyhub.backend.todo.manage.dto.TodoMainCreateRequest;
import com.mydailyhub.backend.todo.manage.dto.TodoMainResponse;
import com.mydailyhub.backend.todo.manage.dto.TodoMainUpdateRequest;
import com.mydailyhub.backend.todo.manage.service.TodoMainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoMainController {

    private final TodoMainService todoMainService;

    @PostMapping
    public ResponseEntity<ApiResponse<TodoMainResponse>> create(
            @Valid @RequestBody TodoMainCreateRequest request) {
        TodoMainResponse todo = todoMainService.create(request);
        URI location = URI.create("/api/todos/" + todo.tdSeq());
        return ResponseEntity.created(location).body(ApiResponse.success(todo));
    }

    @PutMapping("/{tdSeq}")
    public ApiResponse<TodoMainResponse> update(
            @PathVariable("tdSeq") Long tdSeq,
            @Valid @RequestBody TodoMainUpdateRequest request) {
        TodoMainResponse todo = todoMainService.update(tdSeq, request);
        return ApiResponse.success(todo);
    }

    @DeleteMapping("/{tdSeq}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("tdSeq") Long tdSeq) {
        todoMainService.delete(tdSeq);
    }

    @GetMapping("/{tdSeq}")
    public ApiResponse<TodoMainResponse> findById(@PathVariable("tdSeq") Long tdSeq) {
        TodoMainResponse todo = todoMainService.findById(tdSeq);
        return ApiResponse.success(todo);
    }

    @GetMapping
    public ApiResponse<List<TodoMainResponse>> findAll() {
        List<TodoMainResponse> todos = todoMainService.findAll();
        return ApiResponse.success(todos);
    }

    @GetMapping("/admin/{tdSeq}")
    public ApiResponse<TodoMainResponse> findByIdForAdmin(@PathVariable("tdSeq") Long tdSeq) {
        TodoMainResponse todo = todoMainService.findByIdForAdmin(tdSeq);
        return ApiResponse.success(todo);
    }

    @GetMapping("/admin")
    public ApiResponse<List<TodoMainResponse>> findAllForAdmin() {
        List<TodoMainResponse> todos = todoMainService.findAllForAdmin();
        return ApiResponse.success(todos);
    }
}
