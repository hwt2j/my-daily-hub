package com.mydailyhub.backend.common.code.service;

import com.mydailyhub.backend.common.code.dto.CommonCodeCreateRequest;
import com.mydailyhub.backend.common.code.dto.CommonCodeResponse;
import com.mydailyhub.backend.common.code.entity.CommonCode;
import com.mydailyhub.backend.common.code.entity.CommonCodeGroup;
import com.mydailyhub.backend.common.code.repository.CommonCodeGroupRepository;
import com.mydailyhub.backend.common.code.repository.CommonCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonCodeService {

    private final CommonCodeRepository commonCodeRepository;
    private final CommonCodeGroupRepository commonCodeGroupRepository;

    @Transactional
    public CommonCodeResponse create(CommonCodeCreateRequest request) {
        CommonCodeGroup group = commonCodeGroupRepository
                .findByCdGrpSeqAndDeletedFalse(request.cdGrpSeq())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Common code group not found: " + request.cdGrpSeq()));

        CommonCode code = new CommonCode();
        code.setCodeGroup(group);
        code.setCdCode(request.cdCode());
        code.setCdNameKr(request.cdNameKr());
        code.setCdNameEn(request.cdNameEn());
        code.setSortSn(request.sortSn());
        code.setUsed(request.used());
        return CommonCodeResponse.from(commonCodeRepository.save(code));
    }

    public List<CommonCodeResponse> findAll(Long cdGrpSeq) {
        return commonCodeRepository
                .findAllByCodeGroup_CdGrpSeqAndCodeGroup_DeletedFalseAndUsedTrue(
                        cdGrpSeq, Sort.by("sortSn", "cdSeq"))
                .stream()
                .map(CommonCodeResponse::from)
                .toList();
    }
}
