package com.my.notebook.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePostDTO {
    private long accountId;
    private long containerId;
    private long postId;
    private String postTitle;
    private String postContent;
}