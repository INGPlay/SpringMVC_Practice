package com.my.notebook.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class CreatePostForm {

    @Min(1)
    private long containerId;
    @NotEmpty
    @Size(max = 50)
    private String postTitle;

    @NotEmpty
    @Size(max = 1000)
    private String postContent;

    public CreatePostForm(long containerId, String postTitle, String postContent) {
        this.containerId = containerId;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }
}
