package com.mydailyhub.backend.todo.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydailyhub.backend.todo.manage.entity.TodoRoutine;

public interface TodoRoutineRepository extends JpaRepository<TodoRoutine, Long> {
    
}
