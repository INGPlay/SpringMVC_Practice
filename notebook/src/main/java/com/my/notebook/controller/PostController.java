package com.my.notebook.controller;

import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.ids.ACIdsDTO;
import com.my.notebook.domain.ids.ACPIdsDTO;
import com.my.notebook.domain.post.CreatePostDTO;
import com.my.notebook.domain.post.UpdatePostDTO;
import com.my.notebook.service.PostService;
import com.my.notebook.service.SeqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final SeqService seqService;

    public PostController(PostService postService, SeqService seqService) {
        this.postService = postService;
        this.seqService = seqService;
    }

    @GetMapping
    public String retMain(){

        return "redirect:/main";
    }

    @GetMapping("/{containerId}")
    public String createPostForm(@PathVariable long containerId, Model model){
        ACIdsDTO acIdsDTO = new ACIdsDTO();
        acIdsDTO.setAccountId(1);
        acIdsDTO.setContainerId(containerId);
        List<PostDTO> posts = postService.selectPostsByACIds(acIdsDTO);

        model.addAttribute("posts", posts);
        model.addAttribute("acIds", acIdsDTO);
        return "post";
    }

    @PostMapping("/createPost")
    public String createPost(@ModelAttribute CreatePostDTO createPostDTO){
        createPostDTO.setAccountId(1);
        createPostDTO.setPostTitle(" ");
        createPostDTO.setPostContent(" ");

        log.info(String.valueOf(createPostDTO.getAccountId()));
        log.info(String.valueOf(createPostDTO.getContainerId()));
        log.info(createPostDTO.getPostTitle());
        log.info(createPostDTO.getPostContent());

        ACIdsDTO acIdsDTO = new ACIdsDTO();
        acIdsDTO.setAccountId(createPostDTO.getAccountId());
        acIdsDTO.setContainerId(createPostDTO.getContainerId());
        long postId = seqService.getPostIdSeqCurrval(acIdsDTO);

        boolean isCreated = postService.createPostByCreatePostDTO(createPostDTO);
        log.info("포스트 생성");

        return "redirect:/post/" + createPostDTO.getContainerId() + "/" + postId;
    }

    @PostMapping("/updatePost")
    public String updatePost(@ModelAttribute UpdatePostDTO updatePostDTO){
        updatePostDTO.setAccountId(1);
        postService.updatePostByUpdatePostDTO(updatePostDTO);

        log.info("포스트 수정");
        return "redirect:/main/" + updatePostDTO.getContainerId();
    }

    @PostMapping("/deletePost")
    public String deletePost(@ModelAttribute ACPIdsDTO acpIdsDTO){
        acpIdsDTO.setAccountId(1);
        postService.deletePostByACPIdsDTO(acpIdsDTO);

        return "redirect:/main/" + acpIdsDTO.getContainerId();
    }

    @GetMapping("/{containerId}/{postId}")
    public String postPage(@ModelAttribute("containerId") long containerId,
                           @ModelAttribute("postId") long postId,
                           Model model){
        ACIdsDTO acIdsDTO = new ACIdsDTO();
        acIdsDTO.setAccountId(1);
        acIdsDTO.setContainerId(containerId);
        List<PostDTO> posts = postService.selectPostsByACIds(acIdsDTO);

        //temp

        ACPIdsDTO acpIdsDTO = new ACPIdsDTO();
        acpIdsDTO.setAccountId(1);
        acpIdsDTO.setContainerId(containerId);
        acpIdsDTO.setPostId(postId);

        PostDTO post = postService.selectPostByACPIds(acpIdsDTO);

        model.addAttribute("currentPost", post);
        model.addAttribute("posts", posts);
        model.addAttribute("acpIds", acpIdsDTO);

        return "post";
    }
}
