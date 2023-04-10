package com.my.notebook.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePostForm {
    @Min(1)
    private long containerId;

    @Min(1)
    private long postId;

    @NotEmpty
    @Size(max = 50)
    private String postTitle;

    @NotEmpty
    @Size(max = 1000)
    private String postContent;

    public UpdatePostForm(long containerId, long postId, String postTitle, String postContent) {
        this.containerId = containerId;
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }
}
