package com.mydailyhub.backend.todo.manage.dto;

import com.mydailyhub.backend.todo.manage.entity.TodoTag;

public record TodoTagResponse(Long tagSeq, String tagName, boolean deleted) {
    public static TodoTagResponse from(TodoTag tag) {
        return new TodoTagResponse(tag.getTagSeq(), tag.getTagName(), tag.isDeleted());
    }
}
