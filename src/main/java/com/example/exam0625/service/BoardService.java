package com.example.exam0625.service;

import com.example.exam0625.dto.BoardSearchDto;
import com.example.exam0625.dto.BoardWriteDto;
import com.example.exam0625.entity.Board;
import com.example.exam0625.entity.Member;
import com.example.exam0625.mapper.BoardMapper;
import com.example.exam0625.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    @Transactional
    public Board write(BoardWriteDto dto, Member member) {
        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member)
                .build();
        return boardRepository.save(board);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    @Transactional
    public void increaseViewCount(Long id) {
        boardRepository.increaseViewCount(id);
    }

    @Transactional
    public void update(Long id, BoardWriteDto dto, Member loginMember) {
        Board board = findById(id);
        if (!board.getMember().getId().equals(loginMember.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
    }

    @Transactional
    public void delete(Long id, Member loginMember) {
        Board board = findById(id);
        if (!board.getMember().getId().equals(loginMember.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        boardRepository.delete(board);
    }

    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> findByMemberId(Long memberId, Pageable pageable) {
        return boardRepository.findByMemberId(memberId, pageable);
    }

    public List<Board> search(BoardSearchDto searchDto) {
        return boardMapper.searchBoards(searchDto);
    }

    public int countSearch(BoardSearchDto searchDto) {
        return boardMapper.countSearchBoards(searchDto);
    }
}