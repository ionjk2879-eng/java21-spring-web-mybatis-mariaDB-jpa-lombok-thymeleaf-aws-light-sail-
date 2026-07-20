package com.example.exam0625.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private Long memberId;
    private String createdAt;
}
