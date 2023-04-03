package com.my.notebook.controller;

import com.my.notebook.domain.ContainerDTO;
import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.container.CreateContainerDTO;
import com.my.notebook.domain.post.ListPostDTO;
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
        List<ContainerDTO> containers = containerService.listContainerByAccountId(7);

        model.addAttribute("containers", containers);

        return "mainPage";
    }

    @PostMapping({"", "/{containerId}"})
    public String createContainer(@ModelAttribute("containerTitle") String containerTitle){
        CreateContainerDTO createContainerDTO = new CreateContainerDTO();
        createContainerDTO.setAccountId(7);
        createContainerDTO.setContainerTitle(containerTitle);

        boolean isCreated = containerService.createContainer(createContainerDTO);
        log.info("isCreated : " + isCreated);
        log.info(containerTitle);

        // 생성된 컨테이너 페이지로 이동
        long currval = seqService.getContainerIdSeqCurrval();
        return "redirect:/main/" + currval;
    }

    @PostMapping("/deleteContainer")
    public String deleteContainer(@ModelAttribute("containerId") long containerId){
        containerService.deleteContainer(containerId);

        return "redirect:/main";
    }

    @GetMapping("/{containerId}")
    public String mainPage(@PathVariable long containerId, Model model) {
        List<ContainerDTO> containers = containerService.listContainerByAccountId(7);

        ListPostDTO listPostDTO = new ListPostDTO();
        listPostDTO.setAccountId(7);
        listPostDTO.setContainerId(containerId);
        List<PostDTO> posts = postService.listPostByContainerId(listPostDTO);

        model.addAttribute("containers", containers);
        model.addAttribute("posts", posts);
        model.addAttribute("containerId", containerId);

        return "mainPage";
    }
}
