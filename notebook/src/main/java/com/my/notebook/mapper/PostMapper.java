package com.my.notebook.mapper;

import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.post.InsertPostDTO;
import com.my.notebook.domain.post.UpdatePostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    List<PostDTO> selectByContainerId (Long containerId);

    void insertPost(InsertPostDTO InsertPostDTO);

    void updatePost(UpdatePostDTO updatePostDTO);

    void deletePost(@Param("containerId") Long containerId, @Param("id") Long id);
}
