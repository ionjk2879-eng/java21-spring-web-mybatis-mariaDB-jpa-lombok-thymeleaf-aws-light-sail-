package com.example.exam0625.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterDto {

    private String username;
    private String password;
    private String passwordConfirm;
    private String nickname;
    private String email;
}
