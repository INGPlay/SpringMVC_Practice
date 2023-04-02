package com.my.notebook.mapper;

import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.post.InsertPostDTO;
import com.my.notebook.domain.post.UpdatePostDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    SeqMapper seqMapper;

    @Autowired
    ContainerMapper containerMapper;

    @BeforeEach
    public void beforeEach() {
        // Sequence 초기화
        seqMapper.dropContainerIdSeq();
        seqMapper.createContainerIdSeq();

        containerMapper.insertContainer("테스트1");
        containerMapper.insertContainer("테스트2");
        containerMapper.insertContainer("테스트3");

        InsertPostDTO insertPostDTO1 = new InsertPostDTO();
        insertPostDTO1.setContainerId(1L);
        insertPostDTO1.setTitle("마이바티스 테스트");
        insertPostDTO1.setContent("흠흠");

        InsertPostDTO insertPostDTO2 = new InsertPostDTO();
        insertPostDTO2.setContainerId(1L);
        insertPostDTO2.setTitle("마이바티스 테스트");
        insertPostDTO2.setContent("흠흠");

        InsertPostDTO insertPostDTO3 = new InsertPostDTO();
        insertPostDTO3.setContainerId(2L);
        insertPostDTO3.setTitle("마이바티스 테스트");
        insertPostDTO3.setContent("흠흠");
        
        postMapper.insertPost(insertPostDTO1);
        postMapper.insertPost(insertPostDTO2);
        postMapper.insertPost(insertPostDTO3);
    }

    @Test
    public void selectByContainerId() {
        List<PostDTO> posts1 = postMapper.selectByContainerId(1L);
        List<PostDTO> posts2 = postMapper.selectByContainerId(2L);
        List<PostDTO> posts3 = postMapper.selectByContainerId(3L);

        assertThat(posts1.size()).isEqualTo(2);
        assertThat(posts2.size()).isEqualTo(1);
        assertThat(posts3.size()).isEqualTo(0);
    }

    @Test
    public void insertPost(){
        InsertPostDTO insertPostDTO = new InsertPostDTO();
        insertPostDTO.setContainerId(3L);
        insertPostDTO.setTitle("마이바티스 테스트");
        insertPostDTO.setContent("흠흠");

        postMapper.insertPost(insertPostDTO);

        List<PostDTO> posts = postMapper.selectByContainerId(3L);

        assertThat(insertPostDTO.getContainerId()).isEqualTo(posts.get(0).getC_id());
        assertThat(insertPostDTO.getTitle()).isEqualTo(posts.get(0).getP_title());
        assertThat(insertPostDTO.getContent()).isEqualTo(posts.get(0).getP_content());
    }

    @Test
    public void updatePost(){
        UpdatePostDTO updatePostDTO = new UpdatePostDTO();
        updatePostDTO.setContainerId(2L);
        updatePostDTO.setId(1L);
        updatePostDTO.setTitle("마이바티스 업데이트 테스트");
        updatePostDTO.setContent("흠흠");

        postMapper.updatePost(updatePostDTO);

        List<PostDTO> posts = postMapper.selectByContainerId(2L);

        assertThat(updatePostDTO.getContainerId()).isEqualTo(posts.get(0).getC_id());
        assertThat(updatePostDTO.getId()).isEqualTo(posts.get(0).getP_id());
        assertThat(updatePostDTO.getTitle()).isEqualTo(posts.get(0).getP_title());
        assertThat(updatePostDTO.getContent()).isEqualTo(posts.get(0).getP_content());
    }

    @Test
    public void deletePost(){
        postMapper.deletePost(1L, 1L);
        postMapper.deletePost(2L, 1L);

        List<PostDTO> posts1 = postMapper.selectByContainerId(1L);
        List<PostDTO> posts2 = postMapper.selectByContainerId(2L);

        assertThat(posts1.size()).isEqualTo(1);
        assertThat(posts2.size()).isEqualTo(0);
    }

}