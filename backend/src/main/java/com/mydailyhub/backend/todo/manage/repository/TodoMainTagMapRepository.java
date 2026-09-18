package com.mydailyhub.backend.todo.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;
import java.util.List;

import com.mydailyhub.backend.todo.manage.entity.TodoMainTagMap;
import com.mydailyhub.backend.todo.manage.entity.TodoMainTagMapId;

public interface TodoMainTagMapRepository extends JpaRepository<TodoMainTagMap, TodoMainTagMapId> {
    // Derived query: findAllByTodoMain_TdSeqOrderByTagSortSnAsc(Long tdSeq)
    @Query("""
            select m from TodoMainTagMap m join fetch m.todoTag
            where m.todoMain.tdSeq = :id order by m.tagSortSn asc, m.id.tagSeq asc
            """)
    List<TodoMainTagMap> findByTodoId(@Param("id") Long tdSeq);

    // Derived query: findAllByTodoMain_TdSeqInOrderByTagSortSnAsc(Collection<Long> tdSeqs)
    @Query("""
            select m from TodoMainTagMap m join fetch m.todoTag
            where m.todoMain.tdSeq in :ids order by m.tagSortSn asc, m.id.tagSeq asc
            """)
    List<TodoMainTagMap> findByTodoIds(@Param("ids") Collection<Long> tdSeqs);
}
