package com.mydailyhub.backend.common.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import com.mydailyhub.backend.common.code.entity.CommonCode;

import java.util.List;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {
    List<CommonCode> findAllByCodeGroup_CdGrpSeqAndCodeGroup_DeletedFalseAndUsedTrue(
            Long cdGrpSeq, Sort sort);
}
