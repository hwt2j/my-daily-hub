package com.mydailyhub.backend.todo.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydailyhub.backend.todo.manage.entity.TodoRoutineDetail;

public interface TodoRoutineDetailRepository extends JpaRepository<TodoRoutineDetail, Long> {
    
}
