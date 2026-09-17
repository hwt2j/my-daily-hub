// TodoHistoryId.java
package com.mydailyhub.backend.todo.history.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class TodoHistoryId implements Serializable {

    @Column(name = "date_ymd")
    private LocalDate dateYmd;

    @Column(name = "td_seq")
    private Long tdSeq;
}