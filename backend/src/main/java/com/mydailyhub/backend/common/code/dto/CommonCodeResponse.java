package com.mydailyhub.backend.common.code.dto;

import com.mydailyhub.backend.common.code.entity.CommonCode;

import java.time.LocalDateTime;

public record CommonCodeResponse(
        Long cdSeq,
        Long cdGrpSeq,
        String cdCode,
        String cdNameKr,
        String cdNameEn,
        Integer sortSn,
        boolean used,
        LocalDateTime createdDt,
        LocalDateTime updatedDt
) {
    public static CommonCodeResponse from(CommonCode code) {
        return new CommonCodeResponse(
                code.getCdSeq(), code.getCodeGroup().getCdGrpSeq(), code.getCdCode(),
                code.getCdNameKr(), code.getCdNameEn(), code.getSortSn(),
                code.isUsed(), code.getCreatedDt(), code.getUpdatedDt()
        );
    }
}
