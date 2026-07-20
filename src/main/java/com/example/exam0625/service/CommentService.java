package com.example.exam0625.service;

import com.example.exam0625.dto.CommentWriteDto;
import com.example.exam0625.entity.Board;
import com.example.exam0625.entity.Comment;
import com.example.exam0625.entity.Member;
import com.example.exam0625.repository.BoardRepository;
import com.example.exam0625.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Comment write(CommentWriteDto dto, Member member) {
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .board(board)
                .member(member)
                .build();

        return commentRepository.save(comment);
    }

    public List<Comment> findByBoardId(Long boardId) {
        return commentRepository.findByBoardIdOrderByCreatedAtAsc(boardId);
    }

    @Transactional
    public void update(Long id, String content, Member loginMember) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getMember().getId().equals(loginMember.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        comment.setContent(content);
    }

    @Transactional
    public void delete(Long id, Member loginMember) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getMember().getId().equals(loginMember.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
