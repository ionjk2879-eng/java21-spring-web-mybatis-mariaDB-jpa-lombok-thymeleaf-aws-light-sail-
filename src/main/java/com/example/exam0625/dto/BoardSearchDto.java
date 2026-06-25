package com.example.exam0625.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchDto {

    private String searchType;
    private String keyword;
    private int page;
    private int size;

    public int getOffset() {
        return page * size;
    }
}