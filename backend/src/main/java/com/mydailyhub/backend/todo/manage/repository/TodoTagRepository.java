package com.mydailyhub.backend.todo.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mydailyhub.backend.todo.manage.entity.TodoTag;
import java.util.Collection;
import java.util.List;

public interface TodoTagRepository extends JpaRepository<TodoTag, Long> {
    List<TodoTag> findByTagNameIn(Collection<String> tagNames);
}
