package com.my.notebook.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostDTO {
    private long accountId;
    private long containerId;
    private long postId;
    private String postTitle;
    private String postContent;

    public UpdatePostDTO(long accountId, long containerId, long postId, String postTitle, String postContent) {
        this.accountId = accountId;
        this.containerId = containerId;
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }
}
