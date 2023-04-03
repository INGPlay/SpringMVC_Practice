package com.my.notebook.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePostDTO {
    private long containerId;
    private long id;
    private String title;
    private String content;
}
