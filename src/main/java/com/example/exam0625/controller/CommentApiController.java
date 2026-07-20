package com.example.exam0625.controller;

import com.example.exam0625.dto.CommentResponseDto;
import com.example.exam0625.dto.CommentWriteDto;
import com.example.exam0625.entity.Comment;
import com.example.exam0625.entity.Member;
import com.example.exam0625.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @PostMapping
    public ResponseEntity<?> write(@RequestBody CommentWriteDto dto, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        try {
            Comment comment = commentService.write(dto, loginMember);
            return ResponseEntity.ok(CommentResponseDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .nickname(loginMember.getNickname())
                    .memberId(loginMember.getId())
                    .createdAt(comment.getCreatedAt().format(FORMATTER))
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id,
                                   @RequestBody Map<String, String> body,
                                   HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        try {
            commentService.update(id, body.get("content"), loginMember);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");
        if (loginMember == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        try {
            commentService.delete(id, loginMember);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
