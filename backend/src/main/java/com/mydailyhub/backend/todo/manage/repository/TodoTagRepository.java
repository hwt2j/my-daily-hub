package com.mydailyhub.backend.todo.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydailyhub.backend.todo.manage.entity.TodoTag;

public interface TodoTagRepository extends JpaRepository<TodoTag, Long> {
    
}
