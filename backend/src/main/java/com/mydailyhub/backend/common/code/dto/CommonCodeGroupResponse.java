package com.mydailyhub.backend.common.code.dto;

import com.mydailyhub.backend.common.code.entity.CommonCodeGroup;

import java.time.LocalDateTime;

public record CommonCodeGroupResponse(
        Long cdGrpSeq,
        String cdGrpCode,
        String cdGrpName,
        boolean deleted,
        LocalDateTime createdDt,
        LocalDateTime updatedDt
) {
    public static CommonCodeGroupResponse from(CommonCodeGroup group) {
        return new CommonCodeGroupResponse(
                group.getCdGrpSeq(), group.getCdGrpCode(), group.getCdGrpName(),
                group.isDeleted(), group.getCreatedDt(), group.getUpdatedDt()
        );
    }
}
