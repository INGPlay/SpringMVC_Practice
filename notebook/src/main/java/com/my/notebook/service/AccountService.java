package com.my.notebook.service;

import com.my.notebook.domain.AccountDTO;
import com.my.notebook.domain.account.LoginDTO;
import com.my.notebook.mapper.AccountMapper;
import com.my.notebook.mapper.seq.AccountSeqMapper;
import com.my.notebook.mapper.seq.ContainerSeqMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountMapper accountMapper;
    private final AccountSeqMapper accountSeqMapper;
    private final ContainerSeqMapper containerSeqMapper;

    @Autowired
    public AccountService(AccountMapper accountMapper, AccountSeqMapper accountSeqMapper, ContainerSeqMapper containerSeqMapper) {
        this.accountMapper = accountMapper;
        this.accountSeqMapper = accountSeqMapper;
        this.containerSeqMapper = containerSeqMapper;
    }

    public AccountDTO login(LoginDTO loginDTO){
        AccountDTO account = accountMapper.login(loginDTO);

        return account;
    }

    public boolean register(LoginDTO loginDTO){
        try{
            accountMapper.insertAccount(loginDTO);

            long currval = accountSeqMapper.getAccountIdSeqCurrval();
            // 컨테이너 인덱스 세기
            containerSeqMapper.insertContainerIdSeqByAccountId(currval);
        } catch (Exception e){
            return false;
        }
        
        return true;
    }
}
