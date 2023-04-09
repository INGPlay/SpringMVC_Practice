package com.my.notebook.controller;

import com.my.notebook.config.CustomUser;
import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.ids.ACIdsDTO;
import com.my.notebook.domain.ids.ACPIdsDTO;
import com.my.notebook.domain.post.CreatePostDTO;
import com.my.notebook.domain.post.UpdatePostDTO;
import com.my.notebook.service.PostService;
import com.my.notebook.service.SeqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String createPostForm(@AuthenticationPrincipal CustomUser user,
                                 @PathVariable long containerId, Model model){
        ACIdsDTO acIdsDTO = new ACIdsDTO(user.getAccountId(), containerId);
        List<PostDTO> posts = postService.selectPostsByACIds(acIdsDTO);

        model.addAttribute("posts", posts);
        model.addAttribute("acIds", acIdsDTO);
        return "post";
    }

    @PostMapping("/createPost")
    public String createPost(@AuthenticationPrincipal CustomUser user,
                             @ModelAttribute CreatePostDTO createPostDTO){
        createPostDTO.setAccountId(user.getAccountId());
        createPostDTO.setPostContent(" ");

        log.info(String.valueOf(createPostDTO.getAccountId()));
        log.info(String.valueOf(createPostDTO.getContainerId()));
        log.info(createPostDTO.getPostTitle());
        log.info(createPostDTO.getPostContent());

        ACIdsDTO acIdsDTO = new ACIdsDTO(createPostDTO.getAccountId(), createPostDTO.getContainerId());
        long postId = seqService.getPostIdSeqCurrval(acIdsDTO);

        boolean isCreated = postService.createPostByCreatePostDTO(createPostDTO);
        log.info("포스트 생성");

        return "redirect:/post/" + createPostDTO.getContainerId() + "/" + postId;
    }

    @PostMapping("/updatePost")
    public String updatePost(@AuthenticationPrincipal CustomUser user,
                             @ModelAttribute UpdatePostDTO updatePostDTO){
        updatePostDTO.setAccountId(user.getAccountId());
        boolean isUpdate = postService.updatePostByUpdatePostDTO(updatePostDTO);

        log.info("isUpdate : {}", isUpdate);
        log.info("포스트 수정");

        if (!isUpdate){
            return "redirect:/post/" + updatePostDTO.getContainerId() + "/" + updatePostDTO.getPostId();
        }
        return "redirect:/main/" + updatePostDTO.getContainerId();
    }

    @PostMapping("/deletePost")
    public String deletePost(@AuthenticationPrincipal CustomUser user,
                             @ModelAttribute ACPIdsDTO acpIdsDTO){
        acpIdsDTO.setAccountId(user.getAccountId());
        postService.deletePostByACPIdsDTO(acpIdsDTO);

        return "redirect:/main/" + acpIdsDTO.getContainerId();
    }

    @GetMapping("/{containerId}/{postId}")
    public String postPage(@AuthenticationPrincipal CustomUser user,
                           @ModelAttribute("containerId") long containerId,
                           @ModelAttribute("postId") long postId,
                           Model model){

        long accountId = user.getAccountId();

        ACIdsDTO acIdsDTO = new ACIdsDTO(accountId, containerId);
        List<PostDTO> posts = postService.selectPostsByACIds(acIdsDTO);

        ACPIdsDTO acpIdsDTO = new ACPIdsDTO();
        acpIdsDTO.setAccountId(accountId);
        acpIdsDTO.setContainerId(containerId);
        acpIdsDTO.setPostId(postId);

        PostDTO currentPost = postService.selectPostByACPIds(acpIdsDTO);

        model.addAttribute("currentPost", currentPost);
        model.addAttribute("posts", posts);
        model.addAttribute("acpIds", acpIdsDTO);

        return "post";
    }
}
