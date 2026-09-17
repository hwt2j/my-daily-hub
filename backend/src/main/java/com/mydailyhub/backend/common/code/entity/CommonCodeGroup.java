// CommonCodeGroup.java
package com.mydailyhub.backend.common.code.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.mydailyhub.backend.common.base.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "common_code_group")
public class CommonCodeGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_grp_seq")
    private Long cdGrpSeq;

    @Column(name = "cd_grp_code", nullable = false, length = 50, unique = true)
    private String cdGrpCode;

    @Column(name = "cd_grp_name", nullable = false, length = 100)
    private String cdGrpName;

    @Column(name = "del_yn", nullable = false, length = 1)
    private boolean deleted = false;

}