package com.mydailyhub.backend.todo.manage.dto;

import com.mydailyhub.backend.todo.manage.entity.TodoRoutineDetail;

public record TodoRoutineDetailResponse(
        Long rtDtSeq, String rtDetailType, String rtDetailValue, boolean deleted) {
    public static TodoRoutineDetailResponse from(TodoRoutineDetail detail) {
        return new TodoRoutineDetailResponse(
                detail.getRtDtSeq(), detail.getRtDetailType(), detail.getRtDetailValue(), detail.isDeleted());
    }
}
