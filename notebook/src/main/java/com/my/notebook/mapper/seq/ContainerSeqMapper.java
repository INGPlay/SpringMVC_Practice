package com.my.notebook.mapper.seq;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContainerSeqMapper {
    long getContainerIdSeqCurrval();
    void createContainerIdSeq();
    void dropContainerIdSeq();
}
