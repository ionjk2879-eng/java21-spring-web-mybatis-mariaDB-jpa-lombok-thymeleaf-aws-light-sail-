package com.example.exam0625.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentWriteDto {

    private String content;
    private Long boardId;
}
