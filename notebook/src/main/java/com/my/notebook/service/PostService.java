package com.my.notebook.service;

import com.my.notebook.domain.ContainerDTO;
import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.ids.ACIdsDTO;
import com.my.notebook.domain.ids.ACPIdsDTO;
import com.my.notebook.domain.post.CreatePostDTO;
import com.my.notebook.domain.post.UpdatePostDTO;
import com.my.notebook.mapper.PostMapper;
import com.my.notebook.mapper.seq.PostSeqMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@Slf4j
public class PostService {
    private final PostMapper postMapper;
    private final PostSeqMapper postSeqMapper;

    public PostService(PostMapper postMapper, PostSeqMapper postSeqMapper) {
        this.postMapper = postMapper;
        this.postSeqMapper = postSeqMapper;
    }

    public List<PostDTO> selectPostsByACIds(ACIdsDTO acIdsDTO){
        return postMapper.selectPostsByACIds(acIdsDTO);
    }

    public PostDTO selectPostByACPIds(ACPIdsDTO acpIdsDTO){
        return postMapper.selectPostByADPIds(acpIdsDTO);
    }

    public boolean createPostByCreatePostDTO(CreatePostDTO createPostDTO){
        if (isAcceptedPost(createPostDTO.getPostTitle(), createPostDTO.getPostContent())) {
            return false;
        }

        try {
            postMapper.insertPostByCreatePostDTO(createPostDTO);

            // 시퀀스 업데이트
            ACIdsDTO acIdsDTO = new ACIdsDTO(createPostDTO.getAccountId(), createPostDTO.getContainerId());
            postSeqMapper.updatePostIdSeqNextvalByACIds(acIdsDTO);

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean updatePostByUpdatePostDTO(UpdatePostDTO updatePostDTO){
        if (isAcceptedPost(updatePostDTO.getPostTitle(), updatePostDTO.getPostContent())) {
            return false;
        }

        try {
            postMapper.updatePost(updatePostDTO);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static boolean isAcceptedPost(String postTitle, String postContent) {
        if (postTitle.length() > 50) {
            log.info("PostTitle이 김");
            return true;
        } else if (postContent.length() > 1000) {
            log.info("PostContent가 김");
            return true;
        }
        return false;
    }

    public void deletePostByACPIdsDTO(ACPIdsDTO acpIdsDTO){
        postMapper.deletePost(acpIdsDTO);
    }

}
