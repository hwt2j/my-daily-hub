package com.mydailyhub.backend.common.code.controller;

import com.mydailyhub.backend.common.base.response.ApiResponse;
import com.mydailyhub.backend.common.code.dto.CommonCodeCreateRequest;
import com.mydailyhub.backend.common.code.dto.CommonCodeResponse;
import com.mydailyhub.backend.common.code.service.CommonCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common-codes")
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    @GetMapping
    public ApiResponse<List<CommonCodeResponse>> findAll(@RequestParam("cdGrpSeq") Long cdGrpSeq) {
        List<CommonCodeResponse> codes = commonCodeService.findAll(cdGrpSeq);
        return ApiResponse.success(codes);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommonCodeResponse> create(
            @Valid @RequestBody CommonCodeCreateRequest request) {
        CommonCodeResponse code = commonCodeService.create(request);
        return ApiResponse.success(code);
    }
}
