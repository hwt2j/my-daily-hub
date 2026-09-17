package com.mydailyhub.backend.todo.manage.repository;

import com.mydailyhub.backend.todo.manage.entity.TodoMain;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoMainRepository extends JpaRepository<TodoMain, Long> {
    Optional<TodoMain> findByTdSeqAndDeletedFalse(Long tdSeq);

    List<TodoMain> findAllByDeletedFalse(Sort sort);
}
