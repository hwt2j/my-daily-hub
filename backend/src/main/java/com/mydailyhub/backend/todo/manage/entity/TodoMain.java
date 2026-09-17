// TodoMain.java
package com.mydailyhub.backend.todo.manage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.mydailyhub.backend.common.base.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "todo_main")
public class TodoMain extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "td_seq")
    private Long tdSeq;

    @Column(name = "td_name", nullable = false, length = 200)
    private String tdName;

    @Column(name = "td_routine_yn", nullable = false, length = 1)
    private boolean routine = false;

    @Column(name = "td_importance")
    private Integer tdImportance;

    @Column(name = "td_sort_sn")
    private Integer tdSortSn;

    @Column(name = "td_due_dt")
    private LocalDate tdDueDt;

    @Column(name = "del_yn", nullable = false, length = 1)
    private boolean deleted = false;

}