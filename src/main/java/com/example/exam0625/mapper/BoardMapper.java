package com.example.exam0625.mapper;

import com.example.exam0625.dto.BoardSearchDto;
import com.example.exam0625.entity.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<Board> searchBoards(@Param("search") BoardSearchDto searchDto);

    int countSearchBoards(@Param("search") BoardSearchDto searchDto);
}