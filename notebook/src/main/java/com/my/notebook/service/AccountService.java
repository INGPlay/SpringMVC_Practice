package com.my.notebook.service;

import com.my.notebook.domain.AccountDTO;
import com.my.notebook.domain.account.LoginDTO;
import com.my.notebook.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountMapper accountMapper;

    @Autowired
    public AccountService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    public AccountDTO login(LoginDTO loginDTO){
        AccountDTO account = accountMapper.login(loginDTO);

        return account;
    }

    public boolean register(LoginDTO loginDTO){
        try{
            accountMapper.insertAccount(loginDTO);
        } catch (Exception e){
            return false;
        }

        return true;
    }
}
