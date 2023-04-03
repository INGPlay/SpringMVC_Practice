package com.my.notebook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {
    private long a_id;
    private String a_username;
    private String a_password;
    private Date a_created;
}
