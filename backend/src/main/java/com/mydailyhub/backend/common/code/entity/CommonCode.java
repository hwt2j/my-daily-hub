// CommonCode.java
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
@Table(
    name = "common_code",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_common_code_group_code_value",
            columnNames = {"cd_grp_seq", "cd_code"}
        )
    }
)
public class CommonCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_seq")
    private Long cdSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_grp_seq", nullable = false)
    private CommonCodeGroup codeGroup;

    @Column(name = "cd_code", nullable = false, length = 50)
    private String cdCode;

    @Column(name = "cd_name_kr", nullable = false, length = 100)
    private String cdNameKr;

    @Column(name = "cd_name_en", length = 100)
    private String cdNameEn;

    @Column(name = "sort_sn")
    private Integer sortSn;

    @Column(name = "use_yn", nullable = false, length = 1)
    private boolean used = false;

}