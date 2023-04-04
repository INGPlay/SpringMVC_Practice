package com.my.notebook.mapper;

import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.ids.ACIdsDTO;
import com.my.notebook.domain.ids.ACPIdsDTO;
import com.my.notebook.domain.post.CreatePostDTO;
import com.my.notebook.domain.post.UpdatePostDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    @Select("SELECT * FROM post_tbl where a_id = ${accountId} and c_id = ${containerId}")
    List<PostDTO> selectPostsByACIds(ACIdsDTO acIdsDTO);

    @Select("select * from post_tbl where a_id = ${accountId} and c_id = ${containerId} and p_id = ${postId}")
    PostDTO selectPostByADPIds(ACPIdsDTO acpIdsDTO);

    @Insert("insert into post_tbl (a_id, c_id, p_id, p_title, p_content, p_created, p_updated)" +
            "values (" +
            "${accountId}, " +
            "${containerId}, " +
            "(SELECT p_id_seq FROM post_seq_tbl WHERE a_id = ${accountId} AND c_id = ${containerId}), " +
            "#{postTitle}, " +
            "#{postContent}, " +
            "sysdate, " +
            "sysdate" +
            ")")
    void insertPostByCreatePostDTO(CreatePostDTO CreatePostDTO);

    @Update("update post_tbl set " +
            "p_title = #{postTitle}, p_content = #{postContent}" +
            "where " +
            "a_id = ${accountId} and c_id = ${containerId} and p_id = ${postId}")
    void updatePost(UpdatePostDTO updatePostDTO);

    @Delete("delete from post_tbl where a_id = ${accountId} and c_id = ${containerId} and p_id = ${postId}")
    void deletePost(ACPIdsDTO acpIdsDTO);
}
