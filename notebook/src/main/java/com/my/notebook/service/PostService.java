package com.my.notebook.service;

import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.ids.ACIdsDTO;
import com.my.notebook.domain.ids.ACPIdsDTO;
import com.my.notebook.domain.post.CreatePostDTO;
import com.my.notebook.domain.post.UpdatePostForm;
import com.my.notebook.domain.post.UpdatePostDTO;
import com.my.notebook.mapper.PostMapper;
import com.my.notebook.mapper.seq.PostSeqMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public boolean createPostByCreatePostDTO(CreatePostDTO createPostDTO){;
        try {
            postMapper.insertPostByCreatePostDTO(createPostDTO);

            // 시퀀스 업데이트
            ACIdsDTO acIdsDTO = new ACIdsDTO(createPostDTO.getAccountId(), createPostDTO.getContainerId());
            postSeqMapper.updatePostIdSeqNextvalByACIds(acIdsDTO);

            return true;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePostByUpdatePostDTO(UpdatePostDTO updatePostDTO){

        try {
            postMapper.updatePost(updatePostDTO);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deletePostByACPIdsDTO(ACPIdsDTO acpIdsDTO){
        try{
            postMapper.deletePost(acpIdsDTO);
            return true;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

}
