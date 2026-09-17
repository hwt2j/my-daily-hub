// TodoHistory.java
package com.mydailyhub.backend.todo.history.entity;

import com.mydailyhub.backend.common.base.BaseEntity;
import com.mydailyhub.backend.todo.manage.entity.TodoMain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "todo_history")
public class TodoHistory extends BaseEntity {

    @EmbeddedId
    private TodoHistoryId id;

    @MapsId("tdSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "td_seq")
    private TodoMain todoMain;

    @Column(name = "completion_yn", nullable = false, length = 1)
    private boolean completed = false;

    @Column(name = "completion_dt")
    private LocalDateTime completionDt;

}