package com.my.notebook.service;

import com.my.notebook.domain.EncodedAccountDTO;
import com.my.notebook.domain.account.EncodedLoginDTO;
import com.my.notebook.domain.account.LoginDTO;
import com.my.notebook.domain.account.RegisterForm;
import com.my.notebook.mapper.AccountMapper;
import com.my.notebook.mapper.seq.AccountSeqMapper;
import com.my.notebook.mapper.seq.ContainerSeqMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

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

    public Optional<EncodedAccountDTO> selectEncodedPasswordByUsername(String username){
        return Optional.ofNullable(accountMapper.selectEncodedAccountByUsername(username));
    }

    public String register(LoginDTO registerForm){

        String username = registerForm.getUsername();
        String password = registerForm.getPassword();

        EncodedLoginDTO encodedLoginDTO = new EncodedLoginDTO();
        encodedLoginDTO.setUsername(username);
        encodedLoginDTO.setPassword(password);
        encodedLoginDTO.setEncodedPassword(passwordEncoder.encode(password));

        try{
            accountMapper.insertAccount(encodedLoginDTO);
        } catch (DuplicateKeyException de){
            return "중복된 유저이름 입니다.";
        } catch (Exception e){
            e.printStackTrace();
            return "예상치 못한 오류입니다";
        }

        long currval = accountSeqMapper.getAccountIdSeqCurrval();
        // 컨테이너 인덱스 세기
        containerSeqMapper.insertContainerIdSeqByAccountId(currval);
        
        return "ok";
    }
}
