package com.my.notebook.service;

import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.post.ListPostDTO;
import com.my.notebook.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public List<PostDTO> listPostByContainerId(ListPostDTO listPostDTO){
        List<PostDTO> posts = postMapper.selectByContainerId(listPostDTO);

        return posts;
    }
}
