package com.my.notebook.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeqMapper {
    Long getContainerIdSeqCurrval();

    void createContainerIdSeq();

    void dropContainerIdSeq();
}
