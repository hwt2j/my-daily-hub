package com.mydailyhub.backend.todo.manage.dto;

import com.mydailyhub.backend.todo.manage.entity.TodoRoutine;
import com.mydailyhub.backend.todo.manage.entity.TodoRoutineDetail;

import java.time.LocalDate;
import java.util.List;

public record TodoRoutineResponse(
        Long rtSeq,
        String rtRepeatedCycleCode,
        LocalDate rtStartDt,
        LocalDate rtEndDt,
        String rtTimePeriodCode,
        boolean deleted,
        List<TodoRoutineDetailResponse> details
) {
    public static TodoRoutineResponse from(TodoRoutine routine, List<TodoRoutineDetail> details) {
        return new TodoRoutineResponse(routine.getRtSeq(), routine.getRtRepeatedCycleCode(),
                routine.getRtStartDt(), routine.getRtEndDt(), routine.getRtTimePeriodCode(),
                routine.isDeleted(),
                details.stream().map(TodoRoutineDetailResponse::from).toList());
    }
}
