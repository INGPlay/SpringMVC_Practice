package com.my.notebook.mapper;

import com.my.notebook.domain.AccountDTO;
import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.post.InsertPostDTO;
import com.my.notebook.domain.post.ListPostDTO;
import com.my.notebook.domain.post.UpdatePostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostMapper {
    @Select("SELECT * FROM post_tbl where a_id = ${accountId} and c_id = ${containerId}")
    List<PostDTO> selectByContainerId (ListPostDTO listPostDTO);

    void insertPost(InsertPostDTO InsertPostDTO);

    void updatePost(UpdatePostDTO updatePostDTO);

    void deletePost(@Param("containerId") long containerId, @Param("id") Long id);
}
