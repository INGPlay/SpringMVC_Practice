package com.my.notebook.mapper.seq;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountSeqMapper {
    long getAccountIdSeqCurrval();

    /**
     * 테스트를 위한 시퀀스 생성 및 삭제
     */
    void createAccountIdSeq();
    void dropAccountIdSeq();
}
