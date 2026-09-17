package com.mydailyhub.backend.todo.history.repository;

import com.mydailyhub.backend.todo.history.entity.TodoHistory;
import com.mydailyhub.backend.todo.history.entity.TodoHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoHistoryRepository
        extends JpaRepository<TodoHistory, TodoHistoryId> {
}