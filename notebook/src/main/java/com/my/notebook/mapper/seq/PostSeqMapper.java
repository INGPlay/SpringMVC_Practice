package com.my.notebook.mapper.seq;

import com.my.notebook.domain.ids.ACIdsDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostSeqMapper {
    @Select("select p_id_seq from POST_seq_tbl where a_id = ${accountId} and c_id = ${containerId}")
    long getPostIdSeqCurrvalByACIds(ACIdsDTO acIdsDTO);

    @Insert("INSERT into POST_seq_tbl (a_id, c_id, p_id_seq) VALUES (${accountId}, ${containerId}, 1)")
    void insertPostIdSeqByACIds(ACIdsDTO acIdsDTO);

    @Update("UPDATE POST_seq_tbl SET p_id_seq = p_id_seq + 1 WHERE a_id = ${accountId} and c_id = ${containerId}")
    void updatePostIdSeqNextvalByACIds(ACIdsDTO acIdsDTO);
}
