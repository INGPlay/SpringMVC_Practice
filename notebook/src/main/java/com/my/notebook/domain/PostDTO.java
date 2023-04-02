package com.my.notebook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class PostDTO {
    private long c_id;
    private long p_id;
    private String p_title;
    private String p_content;
    private Date p_created;
    private Date p_updated;
}
