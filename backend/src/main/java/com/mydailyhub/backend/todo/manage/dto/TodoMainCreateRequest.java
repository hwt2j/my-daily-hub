package com.mydailyhub.backend.todo.manage.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record TodoMainCreateRequest(
        @NotBlank @Size(max = 200) String tdName,
        boolean routine,
        Integer tdImportance,
        Integer tdSortSn,
        LocalDate tdDueDt,
        @Valid TodoRoutineRequest routineSettings,
        List<String> tagNames
) {
}
