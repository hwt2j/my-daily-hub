package com.mydailyhub.backend.common.code.controller;

import com.mydailyhub.backend.common.base.response.ApiResponse;
import com.mydailyhub.backend.common.code.dto.CommonCodeGroupCreateRequest;
import com.mydailyhub.backend.common.code.dto.CommonCodeGroupResponse;
import com.mydailyhub.backend.common.code.service.CommonCodeGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common-code-groups")
public class CommonCodeGroupController {

    private final CommonCodeGroupService commonCodeGroupService;

    @GetMapping
    public ApiResponse<List<CommonCodeGroupResponse>> findAll() {
        List<CommonCodeGroupResponse> groups = commonCodeGroupService.findAll();
        return ApiResponse.success(groups);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommonCodeGroupResponse> create(
            @Valid @RequestBody CommonCodeGroupCreateRequest request) {
        CommonCodeGroupResponse group = commonCodeGroupService.create(request);
        return ApiResponse.success(group);
    }
}
