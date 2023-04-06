package com.my.notebook.controller;

import com.my.notebook.config.CustomUser;
import com.my.notebook.domain.ContainerDTO;
import com.my.notebook.domain.PostDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/main")
public class ContainerController {

    private final ContainerService containerService;
    private final PostService postService;
    private final SeqService seqService;

    @Autowired
    public ContainerController(ContainerService containerService, PostService postService, SeqService seqService) {
        this.containerService = containerService;
        this.postService = postService;
        this.seqService = seqService;
    }

    // Mapping 함수 시작
    
    @GetMapping
    public String mainPage(@AuthenticationPrincipal CustomUser user,
                           Model model) {
        long accountId = user.getAccountId();
        List<ContainerDTO> containers = containerService.selectContainersByAccountId(accountId);

        model.addAttribute("containers", containers);

        return "mainPage";
    }

    @GetMapping("/{containerId}")
    public String mainPage(@PathVariable long containerId,
                           @AuthenticationPrincipal CustomUser user,
                           Model model) {
        long accountId = user.getAccountId();

        List<ContainerDTO> containers = containerService.selectContainersByAccountId(accountId);

        ACIdsDTO acIdsDTO = new ACIdsDTO();
        acIdsDTO.setAccountId(accountId);
        acIdsDTO.setContainerId(containerId);

        List<PostDTO> posts = postService.selectPostsByACIds(acIdsDTO);
        ContainerDTO currentContainer = containerService.getContainerByContainerId(acIdsDTO);

        model.addAttribute("containers", containers);
        model.addAttribute("posts", posts);
        model.addAttribute("currentContainer", currentContainer);

        return "mainPage";
    }

    @PostMapping({"", "/{containerId}"})
    public String createContainer(@ModelAttribute("containerTitle") String containerTitle,
                                  @AuthenticationPrincipal CustomUser user,
                                  RedirectAttributes redirectAttributes){
        long accountId = user.getAccountId();

        CreateContainerDTO createContainerDTO = new CreateContainerDTO();
        createContainerDTO.setAccountId(accountId);
        createContainerDTO.setContainerTitle(containerTitle);

        // 생성된 컨테이너 페이지로 이동
        boolean isCreated = containerService.createContainer(createContainerDTO);

        redirectAttributes.addAttribute("isCreated", isCreated);

        if (!isCreated){
            return "redirect:/main";
        }

        // 같은 계정에서 동시에 컨테이너 생성할 시 문제가 생길 수도 있을 수 있는 코드인 것 같다
        long createdContainerId = seqService.getContainerIdSeqCurrvalByAccountId(accountId) - 1;

        if (createdContainerId <= 0){
            return "redirect:/main/" + createdContainerId;
        }
        return "redirect:/main/" + createdContainerId;
    }

    @PostMapping("/deleteContainer")
    public String deleteContainer(@ModelAttribute("containerId") long containerId,
                                  @AuthenticationPrincipal CustomUser user){
        long accountId = user.getAccountId();

        ACIdsDTO acIdsDTO = new ACIdsDTO();
        acIdsDTO.setAccountId(accountId);
        acIdsDTO.setContainerId(containerId);

        containerService.deleteContainer(acIdsDTO);

        return "redirect:/main";
    }


}
