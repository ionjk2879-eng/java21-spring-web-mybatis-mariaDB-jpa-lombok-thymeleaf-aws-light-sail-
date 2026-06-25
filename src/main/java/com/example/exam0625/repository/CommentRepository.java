package com.example.exam0625.repository;

import com.example.exam0625.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"member"})
    List<Comment> findByBoardIdOrderByCreatedAtAsc(Long boardId);
}