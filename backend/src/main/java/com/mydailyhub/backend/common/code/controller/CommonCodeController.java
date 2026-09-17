package com.mydailyhub.backend.common.code.controller;

import com.mydailyhub.backend.common.code.dto.CommonCodeCreateRequest;
import com.mydailyhub.backend.common.code.dto.CommonCodeResponse;
import com.mydailyhub.backend.common.code.service.CommonCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common-codes")
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    @GetMapping
    public List<CommonCodeResponse> findAll(@RequestParam("cdGrpSeq") Long cdGrpSeq) {
        return commonCodeService.findAll(cdGrpSeq);
    }

    @PostMapping
    public ResponseEntity<CommonCodeResponse> create(@Valid @RequestBody CommonCodeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commonCodeService.create(request));
    }
}
