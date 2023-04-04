package com.my.notebook.controller;

import com.my.notebook.domain.ContainerDTO;
import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.ids.ACIdsDTO;
import com.my.notebook.domain.container.CreateContainerDTO;
import com.my.notebook.service.ContainerService;
import com.my.notebook.service.PostService;
import com.my.notebook.service.SeqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String mainPage(Model model) {
        List<ContainerDTO> containers = containerService.selectContainersByAccountId(1);

        model.addAttribute("containers", containers);

        return "mainPage";
    }

    @GetMapping("/{containerId}")
    public String mainPage(@PathVariable long containerId, Model model) {
        long tempAccountId = 1L;

        List<ContainerDTO> containers = containerService.selectContainersByAccountId(tempAccountId);

        ACIdsDTO acIdsDTO = new ACIdsDTO();
        acIdsDTO.setAccountId(tempAccountId);
        acIdsDTO.setContainerId(containerId);

        List<PostDTO> posts = postService.selectPostsByACIds(acIdsDTO);
        ContainerDTO currentContainer = containerService.getContainerByContainerId(acIdsDTO);

        model.addAttribute("containers", containers);
        model.addAttribute("posts", posts);
        model.addAttribute("currentContainer", currentContainer);

        return "mainPage";
    }

    @PostMapping({"", "/{containerId}"})
    public String createContainer(@ModelAttribute("containerTitle") String containerTitle){
        long tempAccountId = 1L;

        CreateContainerDTO createContainerDTO = new CreateContainerDTO();
        createContainerDTO.setAccountId(tempAccountId);
        createContainerDTO.setContainerTitle(containerTitle);

        // 생성된 컨테이너 페이지로 이동
        long currval = seqService.getContainerIdSeqCurrvalByAccountId(tempAccountId);

        boolean isCreated = containerService.createContainer(createContainerDTO);
        log.info("isCreated : " + isCreated);
        log.info(containerTitle);

        return "redirect:/main/" + currval;
    }

    @PostMapping("/deleteContainer")
    public String deleteContainer(@ModelAttribute("containerId") long containerId){
        long tempAccountId = 1L;

        ACIdsDTO acIdsDTO = new ACIdsDTO();
        acIdsDTO.setAccountId(tempAccountId);
        acIdsDTO.setContainerId(containerId);

        containerService.deleteContainer(acIdsDTO);

        return "redirect:/main";
    }


}
