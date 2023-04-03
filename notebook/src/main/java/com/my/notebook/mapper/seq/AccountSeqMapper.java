package com.my.notebook.mapper.seq;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountSeqMapper {
    long getAccountIdSeqCurrval();
    void createAccountIdSeq();
    void dropAccountIdSeq();
}
