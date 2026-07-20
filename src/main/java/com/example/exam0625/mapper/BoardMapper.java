package com.example.exam0625.mapper;

import com.example.exam0625.dto.BoardSearchDto;
import com.example.exam0625.entity.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {

    // 검색
    List<Board> searchBoards(@Param("search") BoardSearchDto searchDto);
    int countSearchBoards(@Param("search") BoardSearchDto searchDto);

    // DDL
    void createBoardTable();
    void addViewCountIndex();
    void dropBoardTable();

    // 데이터사전 조회
    List<Map<String, Object>> getIndexList();
    List<Map<String, Object>> getViewList();
}