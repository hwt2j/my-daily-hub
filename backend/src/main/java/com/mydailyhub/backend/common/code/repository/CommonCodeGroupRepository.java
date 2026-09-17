package com.mydailyhub.backend.common.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import com.mydailyhub.backend.common.code.entity.CommonCodeGroup;

import java.util.List;
import java.util.Optional;

public interface CommonCodeGroupRepository extends JpaRepository<CommonCodeGroup, Long> {
    List<CommonCodeGroup> findAllByDeletedFalse(Sort sort);

    Optional<CommonCodeGroup> findByCdGrpSeqAndDeletedFalse(Long cdGrpSeq);
}
