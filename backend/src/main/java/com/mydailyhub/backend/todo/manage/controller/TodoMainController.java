package com.mydailyhub.backend.todo.manage.controller;

import com.mydailyhub.backend.todo.manage.dto.TodoMainCreateRequest;
import com.mydailyhub.backend.todo.manage.dto.TodoMainResponse;
import com.mydailyhub.backend.todo.manage.dto.TodoMainUpdateRequest;
import com.mydailyhub.backend.todo.manage.service.TodoMainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoMainController {

    private final TodoMainService todoMainService;

    @PostMapping
    public ResponseEntity<TodoMainResponse> create(@Valid @RequestBody TodoMainCreateRequest request) {
        TodoMainResponse response = todoMainService.create(request);
        return ResponseEntity.created(URI.create("/api/todos/" + response.tdSeq()))
                .body(response);
    }

    @PutMapping("/{tdSeq}")
    public TodoMainResponse update(@PathVariable("tdSeq") Long tdSeq,
                                   @Valid @RequestBody TodoMainUpdateRequest request) {
        return todoMainService.update(tdSeq, request);
    }

    @DeleteMapping("/{tdSeq}")
    public ResponseEntity<Void> delete(@PathVariable("tdSeq") Long tdSeq) {
        todoMainService.delete(tdSeq);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{tdSeq}")
    public TodoMainResponse findById(@PathVariable("tdSeq") Long tdSeq) {
        return todoMainService.findById(tdSeq);
    }

    @GetMapping
    public List<TodoMainResponse> findAll() {
        return todoMainService.findAll();
    }

    @GetMapping("/admin/{tdSeq}")
    public TodoMainResponse findByIdForAdmin(@PathVariable("tdSeq") Long tdSeq) {
        return todoMainService.findByIdForAdmin(tdSeq);
    }

    @GetMapping("/admin")
    public List<TodoMainResponse> findAllForAdmin() {
        return todoMainService.findAllForAdmin();
    }
}
