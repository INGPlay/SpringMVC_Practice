package com.my.notebook.service;

import com.my.notebook.domain.AccountDTO;
import com.my.notebook.domain.EncodedAccountDTO;
import com.my.notebook.domain.account.EncodedLoginDTO;
import com.my.notebook.domain.account.LoginDTO;
import com.my.notebook.mapper.AccountMapper;
import com.my.notebook.mapper.seq.AccountSeqMapper;
import com.my.notebook.mapper.seq.ContainerSeqMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AccountService {
    private final AccountMapper accountMapper;
    private final AccountSeqMapper accountSeqMapper;
    private final ContainerSeqMapper containerSeqMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountMapper accountMapper, AccountSeqMapper accountSeqMapper, ContainerSeqMapper containerSeqMapper, PasswordEncoder passwordEncoder) {
        this.accountMapper = accountMapper;
        this.accountSeqMapper = accountSeqMapper;
        this.containerSeqMapper = containerSeqMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AccountDTO> selectAccountByUsername(String username){
        return Optional.ofNullable(accountMapper.selectByUsername(username));
    }

    public Optional<EncodedAccountDTO> selectEncodedPasswordByUsername(String username){
        return Optional.ofNullable(accountMapper.selectEncodedAccountByUsername(username));
    }

    public AccountDTO login(LoginDTO loginDTO){
        AccountDTO account = accountMapper.login(loginDTO);

        return account;
    }

    public boolean register(LoginDTO loginDTO){
        try{
            EncodedLoginDTO encodedLoginDTO = new EncodedLoginDTO();
            encodedLoginDTO.setUsername(loginDTO.getUsername());
            encodedLoginDTO.setPassword(loginDTO.getPassword());
            encodedLoginDTO.setEncodedPassword(passwordEncoder.encode(loginDTO.getPassword()));

            log.info("aaaa : {}", encodedLoginDTO.getUsername());
            log.info("aaaa : {}", encodedLoginDTO.getPassword());
            log.info("aaaa : {}", encodedLoginDTO.getEncodedPassword());

            accountMapper.insertAccount(encodedLoginDTO);

            long currval = accountSeqMapper.getAccountIdSeqCurrval();
            // 컨테이너 인덱스 세기
            containerSeqMapper.insertContainerIdSeqByAccountId(currval);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
}
