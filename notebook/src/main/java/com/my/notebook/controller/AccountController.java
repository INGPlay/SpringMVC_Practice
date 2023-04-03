package com.my.notebook.controller;

import com.my.notebook.domain.AccountDTO;
import com.my.notebook.domain.account.LoginDTO;
import com.my.notebook.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String loginPage(){

        return "login/loginForm";
    }

    @PostMapping
    public String login(LoginDTO loginDTO){
        AccountDTO accountInfo = accountService.login(loginDTO);

        if (accountInfo == null){
            log.info("로그인 실패");
            return "redirect:/login";
        }

        log.info("로그인 성공");
        return "redirect:/main";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "login/registerForm";
    }

    @PostMapping("/register")
    public String register(LoginDTO loginDTO, Model model){

        boolean isRegistered = accountService.register(loginDTO);

        if (isRegistered){
            return "redirect:/login";
        } else {
            model.addAttribute("isRegistered", isRegistered);
            return "redirect:/login/register";
        }
    }
}
