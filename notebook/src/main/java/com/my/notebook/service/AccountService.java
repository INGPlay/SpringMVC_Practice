package com.my.notebook.service;

import com.my.notebook.domain.AccountDTO;
import com.my.notebook.domain.EncodedAccountDTO;
import com.my.notebook.domain.account.EncodedLoginDTO;
import com.my.notebook.domain.account.LoginDTO;
import com.my.notebook.domain.account.RegisterDTO;
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

    public String register(RegisterDTO registerDTO){

        Pattern usernamePattern = Pattern.compile("^([a-z0-9]{4,10})$");
        Pattern passwordPattern = Pattern.compile("^([A-Za-z0-9!@#$%]{4,20})$");

        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();
        String passwordCheck = registerDTO.getPasswordCheck();

        if (!usernamePattern.matcher(username).matches()){
            return "잘못된 username 입니다 (4-10 문자, 영어 소문자, 아라비아 숫자)";
        }
        if (!passwordPattern.matcher(password).matches()){
            return "양식에 맞지 않는 password 입니다 (4~20 문자, 영어 대문자, 소문자, 아라비아 숫자, !,@,#,$,% 사용 가능)";
        }
        if (!passwordCheck.equals(password)){
            return "Password 와 PasswordCheck가 일치하지 않습니다";
        }

        EncodedLoginDTO encodedLoginDTO = new EncodedLoginDTO();
        encodedLoginDTO.setUsername(username);
        encodedLoginDTO.setPassword(password);
        encodedLoginDTO.setEncodedPassword(passwordEncoder.encode(password));

        try{
            accountMapper.insertAccount(encodedLoginDTO);
        } catch (DuplicateKeyException de){
            return "중복된 username 입니다.";
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
