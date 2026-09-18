package com.mydailyhub.backend.todo.manage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record TodoRoutineRequest(
        @NotBlank @Size(max = 20) String rtRepeatedCycleCode,
        @NotNull LocalDate rtStartDt,
        LocalDate rtEndDt,
        @NotBlank @Size(max = 20) String rtTimePeriodCode,
        // Detail validation depends on rtRepeatedCycleCode and is handled by the service.
        List<TodoRoutineDetailRequest> details
) {
}
