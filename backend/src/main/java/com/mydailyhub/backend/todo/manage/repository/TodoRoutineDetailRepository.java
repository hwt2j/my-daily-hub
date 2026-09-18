package com.mydailyhub.backend.todo.manage.repository;

import com.mydailyhub.backend.todo.manage.entity.TodoRoutineDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TodoRoutineDetailRepository extends JpaRepository<TodoRoutineDetail, Long> {
    // Derived query: findAllByTodoRoutine_RtSeqOrderByRtDtSeqAsc(Long rtSeq)
    @Query("select d from TodoRoutineDetail d where d.todoRoutine.rtSeq = :id order by d.rtDtSeq asc")
    List<TodoRoutineDetail> findByRoutineId(@Param("id") Long rtSeq);

    // Derived query: findAllByTodoRoutine_RtSeqAndDeletedFalseOrderByRtDtSeqAsc(Long rtSeq)
    @Query("""
            select d from TodoRoutineDetail d
            where d.todoRoutine.rtSeq = :id and d.deleted = false
            order by d.rtDtSeq asc
            """)
    List<TodoRoutineDetail> findActiveByRoutineId(@Param("id") Long rtSeq);

    // Derived query: findAllByTodoRoutine_RtSeqInOrderByRtDtSeqAsc(Collection<Long> rtSeqs)
    @Query("select d from TodoRoutineDetail d where d.todoRoutine.rtSeq in :ids order by d.rtDtSeq asc")
    List<TodoRoutineDetail> findByRoutineIds(@Param("ids") Collection<Long> rtSeqs);

    // Derived query: findAllByTodoRoutine_RtSeqInAndDeletedFalseOrderByRtDtSeqAsc(Collection<Long> rtSeqs)
    @Query("""
            select d from TodoRoutineDetail d
            where d.todoRoutine.rtSeq in :ids and d.deleted = false
            order by d.rtDtSeq asc
            """)
    List<TodoRoutineDetail> findActiveByRoutineIds(@Param("ids") Collection<Long> rtSeqs);
}
