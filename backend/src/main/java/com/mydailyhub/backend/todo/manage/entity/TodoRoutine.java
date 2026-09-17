// TodoRoutine.java
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
@Table(
    name = "todo_routine",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_todo_routine_td_seq",
            columnNames = "td_seq"
        )
    }
)
public class TodoRoutine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rt_seq")
    private Long rtSeq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "td_seq", nullable = false, unique = true)
    private TodoMain todoMain;

    @Column(name = "rt_repeated_cycle_code", nullable = false, length = 20)
    private String rtRepeatedCycleCode;

    @Column(name = "rt_start_dt", nullable = false)
    private LocalDate rtStartDt;

    @Column(name = "rt_end_dt")
    private LocalDate rtEndDt;

    @Column(name = "rt_time_period_code", length = 20)
    private String rtTimePeriodCode;

    @Column(name = "del_yn", nullable = false, length = 1)
    private boolean deleted = false;
}