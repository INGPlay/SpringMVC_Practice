package com.my.notebook.controller;

import com.my.notebook.config.CustomUser;
import com.my.notebook.domain.ContainerDTO;
import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.container.CreateContainerForm;
import com.my.notebook.domain.ids.ACIdsDTO;
import com.my.notebook.domain.container.CreateContainerDTO;
import com.my.notebook.service.ContainerService;
import com.my.notebook.service.PostService;
import com.my.notebook.service.SeqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/main")
public class ContainerController {

    private final ContainerService containerService;
    private final SeqService seqService;

    @Autowired
    public ContainerController(ContainerService containerService, SeqService seqService) {
        this.containerService = containerService;
        this.seqService = seqService;
    }

    // Mapping 함수 시작
    
    @GetMapping
    public String mainPage(@AuthenticationPrincipal CustomUser user,
                           Model model) {
        List<ContainerDTO> containers = containerService.selectContainersByAccountId(user.getAccountId());

        model.addAttribute("containers", containers);
        model.addAttribute("createContainerForm", new CreateContainerForm(""));

        return "mainPage";
    }

    @PostMapping
    public String createContainerInEmpty(@Validated @ModelAttribute CreateContainerForm createContainerForm,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal CustomUser user,
                                         RedirectAttributes redirectAttributes,
                                         Model model){

        log.info("{}", bindingResult);
        // Validation
        if (bindingResult.hasErrors()){
            List<ContainerDTO> containers = containerService.selectContainersByAccountId(user.getAccountId());

            model.addAttribute("containers", containers);
            model.addAttribute("createContainerForm", createContainerForm);

            return "mainPage";
        }

        // 컨테이너 생성
        CreateContainerDTO createContainerDTO = new CreateContainerDTO(
                user.getAccountId(),
                createContainerForm.getContainerTitle()
        );
        boolean isCreateContainer = containerService.createContainer(createContainerDTO);

        redirectAttributes.addAttribute("isCreateContainer", isCreateContainer);

        if (!isCreateContainer){
            return "redirect:/main";
        }

        // 생성된 저장소의 키를 가져온다
        // 같은 계정에서 동시에 컨테이너 생성할 시 문제가 생길 수도 있을 수 있는 코드인 것 같다
        long createdContainerId = seqService.getContainerIdSeqCurrvalByAccountId(user.getAccountId()) - 1;

        return "redirect:/main/" + createdContainerId;
    }

    @GetMapping("/{containerId}")
    public String mainPage(@PathVariable long containerId,
                           @AuthenticationPrincipal CustomUser user,
                           Model model) {

        ACIdsDTO acIdsDTO = new ACIdsDTO(user.getAccountId(), containerId);
        containerService.setMainPage(acIdsDTO, model);

        model.addAttribute("createContainerForm", new CreateContainerForm(""));

        return "mainPage";
    }

    @PostMapping("/{containerId}")
    public String createContainer(@Validated @ModelAttribute CreateContainerForm createContainerForm,
                                  BindingResult bindingResult,
                                  @PathVariable long containerId,
                                  @AuthenticationPrincipal CustomUser user,
                                  RedirectAttributes redirectAttributes,
                                  Model model){

        log.info("{}", bindingResult);
        // Validation
        if (bindingResult.hasErrors()){
            ACIdsDTO acIdsDTO = new ACIdsDTO(user.getAccountId(), containerId);
            containerService.setMainPage(acIdsDTO, model);

            model.addAttribute("createContainerForm", createContainerForm);

            return "mainPage";
        }

        // 컨테이너 생성
        CreateContainerDTO createContainerDTO = new CreateContainerDTO(
                user.getAccountId(),
                createContainerForm.getContainerTitle()
        );

        // 생성된 컨테이너 페이지로 이동
        boolean isCreateContainer = containerService.createContainer(createContainerDTO);

        redirectAttributes.addAttribute("isCreateContainer", isCreateContainer);

        if (!isCreateContainer){
            return "redirect:/main";
        }

        // 생성된 저장소의 키를 가져온다
        // 같은 계정에서 동시에 컨테이너 생성할 시 문제가 생길 수도 있을 수 있는 코드인 것 같다
        long createdContainerId = seqService.getContainerIdSeqCurrvalByAccountId(user.getAccountId()) - 1;

        return "redirect:/main/" + createdContainerId;
    }

    @PostMapping("/deleteContainer")
    public String deleteContainer(@ModelAttribute("containerId") long containerId,
                                  @AuthenticationPrincipal CustomUser user,
                                  RedirectAttributes redirectAttributes){

        ACIdsDTO acIdsDTO = new ACIdsDTO(user.getAccountId(), containerId);
        boolean isDeleteContainer = containerService.deleteContainer(acIdsDTO);

        redirectAttributes.addAttribute("isDeleteContainer", isDeleteContainer);
        return "redirect:/main";
    }

}
