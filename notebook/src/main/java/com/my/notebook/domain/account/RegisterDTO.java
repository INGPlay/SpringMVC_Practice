package com.my.notebook.domain.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterDTO {
    private String username;
    private String password;
    private String passwordCheck;
}