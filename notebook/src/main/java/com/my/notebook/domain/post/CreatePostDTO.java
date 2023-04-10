package com.my.notebook.domain.post;

import lombok.Getter;

@Getter
public class CreatePostDTO {
    private long accountId;
    private long containerId;
    private String postTitle;
    private String postContent;

    public CreatePostDTO(long accountId, long containerId, String postTitle, String postContent) {
        this.accountId = accountId;
        this.containerId = containerId;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }
}
