package com.my.notebook.mapper.seq;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostSeqMapper {
    long getPostIdSeqCurrval();
    void createPostIdSeq();
    void dropPostIdSeq();
}
