package com.mydailyhub.backend.common.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CommonCodeCreateRequest(
        @NotNull @Positive Long cdGrpSeq,
        @NotBlank @Size(max = 50) String cdCode,
        @NotBlank @Size(max = 100) String cdNameKr,
        @Size(max = 100) String cdNameEn,
        Integer sortSn,
        boolean used
) {
}
