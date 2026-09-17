// TodoMainTagMapId.java
package com.mydailyhub.backend.todo.manage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class TodoMainTagMapId implements Serializable {

    @Column(name = "td_seq")
    private Long tdSeq;

    @Column(name = "tag_seq")
    private Long tagSeq;
}