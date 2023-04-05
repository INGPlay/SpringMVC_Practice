package com.my.notebook.domain.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SecurityLoginDTO {
    private String username;
    private String password;
    private String roles;
}
