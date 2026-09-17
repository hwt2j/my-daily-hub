package com.mydailyhub.backend.todo.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydailyhub.backend.todo.manage.entity.TodoMainTagMap;
import com.mydailyhub.backend.todo.manage.entity.TodoMainTagMapId;

public interface TodoMainTagMapRepository extends JpaRepository<TodoMainTagMap, TodoMainTagMapId> {
    
}
