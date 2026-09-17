// TodoTag.java
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
@Table(name = "todo_tag")
public class TodoTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_seq")
    private Long tagSeq;

    @Column(name = "tag_name", nullable = false, length = 100)
    private String tagName;

    @Column(name = "del_yn", nullable = false, length = 1)
    private boolean deleted = false;

}