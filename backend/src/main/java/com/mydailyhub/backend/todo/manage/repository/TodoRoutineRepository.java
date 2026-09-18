package com.mydailyhub.backend.todo.manage.repository;

import com.mydailyhub.backend.todo.manage.entity.TodoRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TodoRoutineRepository extends JpaRepository<TodoRoutine, Long> {
    // Derived query: findByTodoMain_TdSeq(Long tdSeq)
    @Query("select r from TodoRoutine r where r.todoMain.tdSeq = :id")
    Optional<TodoRoutine> findByTodoId(@Param("id") Long tdSeq);

    // Derived query: findByTodoMain_TdSeqAndDeletedFalse(Long tdSeq)
    @Query("select r from TodoRoutine r where r.todoMain.tdSeq = :id and r.deleted = false")
    Optional<TodoRoutine> findActiveByTodoId(@Param("id") Long tdSeq);

    // Derived query: findAllByTodoMain_TdSeqIn(Collection<Long> tdSeqs)
    @Query("select r from TodoRoutine r where r.todoMain.tdSeq in :ids")
    List<TodoRoutine> findByTodoIds(@Param("ids") Collection<Long> tdSeqs);

    // Derived query: findAllByTodoMain_TdSeqInAndDeletedFalse(Collection<Long> tdSeqs)
    @Query("select r from TodoRoutine r where r.todoMain.tdSeq in :ids and r.deleted = false")
    List<TodoRoutine> findActiveByTodoIds(@Param("ids") Collection<Long> tdSeqs);
}
