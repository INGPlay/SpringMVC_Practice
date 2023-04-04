package com.my.notebook.mapper.seq;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ContainerSeqMapper {
    @Select("select c_id_seq from CONTAINER_seq_tbl where a_id = ${accountId}")
    long getContainerIdSeqCurrvalByAccountId(long accountId);

    @Insert("INSERT into container_seq_tbl (a_id, c_id_seq) VALUES (${accountId}, 1)")
    void insertContainerIdSeqByAccountId(long accountId);

    @Update("UPDATE CONTAINER_seq_tbl SET c_id_seq = c_id_seq + 1 WHERE a_id = ${accountId}")
    void updateContainerIdSeqNextvalByAccountId(long accountid);
}
