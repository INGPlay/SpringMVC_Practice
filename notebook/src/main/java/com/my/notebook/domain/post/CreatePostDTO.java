package com.my.notebook.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatePostDTO {
    private long accountId;
    private long containerId;
    private String postTitle;
    private String postContent;
}
