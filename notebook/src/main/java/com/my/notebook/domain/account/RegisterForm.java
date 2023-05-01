package com.my.notebook.domain.account;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Getter
@Setter
public class RegisterForm {

    public RegisterForm(String username, String password, String passwordCheck) {
        this.username = username;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }

    /**'
     * @Range를 Pattern에서 정규표현식으로 빼고 길이 제한을 할 수도 있지만
     * 길이 에러 메시지와 패턴 메시지를 따로 주기 위해 다 붙임
     */
    @NotNull
    @Size(min = 4, max = 10)
    @NotNull
    @Size(min = 4, max = 10)
    @Pattern(regexp = "^([a-z0-9]*)$")
    @Pattern(regexp = "^([a-z0-9]*)$")
    private String username;

    @NotNull
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^([A-Za-z0-9!@#$%]*)$")
    private String password;

    @NotNull
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^([A-Za-z0-9!@#$%]*)$")
    private String passwordCheck;
}
