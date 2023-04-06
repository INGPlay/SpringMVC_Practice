package com.my.notebook.controller;

import com.my.notebook.domain.AccountDTO;
import com.my.notebook.domain.account.LoginDTO;
import com.my.notebook.domain.account.RegisterDTO;
import com.my.notebook.service.AccountService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/register")
    public String registerPage(@RequestParam(required = false) String registerMessage){
        return "login/registerForm";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("username") String username,
                           @ModelAttribute("password") String password,
                           @ModelAttribute("passwordCheck") String passwordCheck,
                           RedirectAttributes redirectAttributes){

        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername(username);
        registerDTO.setPassword(password);
        registerDTO.setPasswordCheck(passwordCheck);
        String registerMessage = accountService.register(registerDTO);

        log.info(registerMessage);
        // 쿼리 스트링 추가
        redirectAttributes.addAttribute("registerMessage", registerMessage);

        if (registerMessage.equals("ok")){
            return "redirect:/login";
        } else {
            return "redirect:/login/register";
        }
    }
}
