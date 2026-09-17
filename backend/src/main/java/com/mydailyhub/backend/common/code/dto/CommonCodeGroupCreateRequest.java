package com.mydailyhub.backend.common.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommonCodeGroupCreateRequest(
        @NotBlank @Size(max = 50) String cdGrpCode,
        @NotBlank @Size(max = 100) String cdGrpName
) {
}
