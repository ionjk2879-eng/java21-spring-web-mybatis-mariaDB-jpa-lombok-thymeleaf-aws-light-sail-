package com.example.exam0625.repository;

import com.example.exam0625.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    @EntityGraph(attributePaths = {"member"})
    Page<Board> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"member"})
    Page<Board> findByMemberId(Long memberId, Pageable pageable);

    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    void increaseViewCount(@Param("id") Long id);
}