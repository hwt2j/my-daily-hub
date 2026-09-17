// TodoMainTagMap.java
package com.mydailyhub.backend.todo.manage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "todo_main_tag_map")
public class TodoMainTagMap {

    @EmbeddedId
    private TodoMainTagMapId id;

    @MapsId("tdSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "td_seq")
    private TodoMain todoMain;

    @MapsId("tagSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_seq")
    private TodoTag todoTag;

    @Column(name = "tag_sort_sn")
    private Integer tagSortSn;

    @Column(name = "created_dt", nullable = false)
    private LocalDateTime createdDt;
}