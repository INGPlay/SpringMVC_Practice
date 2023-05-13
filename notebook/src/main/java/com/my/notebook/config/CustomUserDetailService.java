package com.my.notebook.config;

import com.my.notebook.domain.EncodedAccountDTO;
import com.my.notebook.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomUserDetailService implements UserDetailsService {
    private final AccountService accountService;

    @Autowired
    public CustomUserDetailService(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<EncodedAccountDTO> accountOptional = accountService.selectEncodedPasswordByUsername(username);
        EncodedAccountDTO account = accountOptional.orElseThrow(
                ()-> new UsernameNotFoundException("없는 회원입니다.")
        );

        Collection authorities = getAuthorities(account);

        CustomUser user = new CustomUser(
                account.getA_username(),
                account.getA_encodedPassword(),
                authorities,
                account.getA_id()
        );

        return user;
    }

    private static Collection getAuthorities(EncodedAccountDTO account) {
        Collection authorities = new ArrayList<>();

        if (account.getA_isAdmin().equals("1")){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (account.getA_isAdmin().equals("0")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            System.out.println("---------------예외 상황----------------");
        }
        return authorities;
    }
}
