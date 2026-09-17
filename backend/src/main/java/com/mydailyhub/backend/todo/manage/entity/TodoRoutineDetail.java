// TodoRoutineDetail.java
package com.mydailyhub.backend.todo.manage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.mydailyhub.backend.common.base.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "todo_routine_detail")
public class TodoRoutineDetail  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rt_dt_seq")
    private Long rtDtSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rt_seq", nullable = false)
    private TodoRoutine todoRoutine;

    @Column(name = "rt_detail_type", nullable = false, length = 30)
    private String rtDetailType;

    @Column(name = "rt_detail_value", nullable = false, length = 30)
    private String rtDetailValue;

    @Column(name = "del_yn", nullable = false, length = 1)
    private boolean deleted = false;
}