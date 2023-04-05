package com.my.notebook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EncodedAccountDTO {
    private long a_id;
    private String a_username;
    private String a_password;
    private String a_encodedPassword;
    private Date a_created;
    private String a_isAdmin;
}
