package com.my.notebook.controller;

import com.my.notebook.domain.account.LoginDTO;
import com.my.notebook.domain.account.RegisterForm;
import com.my.notebook.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public String loginPage(Model model){
        model.addAttribute("loginForm", new LoginDTO("", ""));
        return "login/loginForm";
    }

    @GetMapping("/register")
    public String registerPage(Model model){

        model.addAttribute("registerForm", new RegisterForm("", "", ""));

        return "login/registerForm";
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("registerForm") RegisterForm registerForm,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){
        log.info("errors = {}", bindingResult);

        // Validation
        if (!registerForm.getPassword().equals(registerForm.getPasswordCheck())){
            bindingResult.reject("passwordCheck", null, "비밀번호 확인이 맞지 않습니다.");
            return "login/registerForm";
        }

        if (bindingResult.hasErrors()){
            return "login/registerForm";
        }

        // Service
        LoginDTO loginDTO = new LoginDTO(registerForm.getUsername(), registerForm.getPassword());
        String registerMessage = accountService.register(loginDTO);

        // Looting
        // 쿼리 스트링 추가
        redirectAttributes.addAttribute("registerMessage", registerMessage);

        if (registerMessage.equals("ok")){
            return "redirect:/login";
        } else {
            bindingResult.reject("serviceError", null, registerMessage);
            return "login/registerForm";
        }
    }
}
