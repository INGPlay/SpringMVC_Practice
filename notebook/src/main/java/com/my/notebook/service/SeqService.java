package com.my.notebook.service;

import com.my.notebook.domain.AccountDTO;
import com.my.notebook.domain.account.LoginDTO;
import com.my.notebook.mapper.AccountMapper;
import com.my.notebook.mapper.seq.AccountSeqMapper;
import com.my.notebook.mapper.seq.ContainerSeqMapper;
import com.my.notebook.mapper.seq.PostSeqMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeqService {
    private final AccountSeqMapper accountSeqMapper;
    private final ContainerSeqMapper containerSeqMapper;
    private final PostSeqMapper postSeqMapper;

    public SeqService(AccountSeqMapper accountSeqMapper, ContainerSeqMapper containerSeqMapper, PostSeqMapper postSeqMapper) {
        this.accountSeqMapper = accountSeqMapper;
        this.containerSeqMapper = containerSeqMapper;
        this.postSeqMapper = postSeqMapper;
    }

    public long getAccaountIdSeqCurrval(){
        return accountSeqMapper.getAccountIdSeqCurrval();
    }

    public long getContainerIdSeqCurrval(){
        return containerSeqMapper.getContainerIdSeqCurrval();
    }

    public long getPostIdSeqCurrval(){
        return postSeqMapper.getPostIdSeqCurrval();
    }

}
