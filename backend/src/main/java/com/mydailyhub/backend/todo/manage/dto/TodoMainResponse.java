package com.mydailyhub.backend.todo.manage.dto;

import com.mydailyhub.backend.todo.manage.entity.TodoMain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TodoMainResponse(
        Long tdSeq,
        String tdName,
        boolean routine,
        Integer tdImportance,
        Integer tdSortSn,
        LocalDate tdDueDt,
        boolean deleted,
        LocalDateTime createdDt,
        LocalDateTime updatedDt,
        TodoRoutineResponse routineSettings,
        List<TodoTagResponse> tags
) {
    public static TodoMainResponse from(
            TodoMain todo, TodoRoutineResponse routineSettings, List<TodoTagResponse> tags) {
        return new TodoMainResponse(
                todo.getTdSeq(), todo.getTdName(), todo.isRoutine(),
                todo.getTdImportance(), todo.getTdSortSn(), todo.getTdDueDt(),
                todo.isDeleted(), todo.getCreatedDt(), todo.getUpdatedDt(), routineSettings, tags
        );
    }
}
