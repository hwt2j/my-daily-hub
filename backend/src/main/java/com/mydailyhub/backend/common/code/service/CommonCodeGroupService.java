package com.mydailyhub.backend.common.code.service;

import com.mydailyhub.backend.common.code.dto.CommonCodeGroupCreateRequest;
import com.mydailyhub.backend.common.code.dto.CommonCodeGroupResponse;
import com.mydailyhub.backend.common.code.entity.CommonCodeGroup;
import com.mydailyhub.backend.common.code.repository.CommonCodeGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonCodeGroupService {

    private final CommonCodeGroupRepository commonCodeGroupRepository;

    @Transactional
    public CommonCodeGroupResponse create(CommonCodeGroupCreateRequest request) {
        CommonCodeGroup group = new CommonCodeGroup();
        group.setCdGrpCode(request.cdGrpCode());
        group.setCdGrpName(request.cdGrpName());
        return CommonCodeGroupResponse.from(commonCodeGroupRepository.save(group));
    }

    public List<CommonCodeGroupResponse> findAll() {
        return commonCodeGroupRepository.findAllByDeletedFalse(Sort.by("cdGrpSeq"))
                .stream()
                .map(CommonCodeGroupResponse::from)
                .toList();
    }
}
