package com.my.notebook.controller;

import com.my.notebook.config.CustomUser;
import com.my.notebook.domain.PostDTO;
import com.my.notebook.domain.ids.ACIdsDTO;
import com.my.notebook.domain.ids.ACPIdsDTO;
import com.my.notebook.domain.post.CreatePostDTO;
import com.my.notebook.domain.post.CreatePostForm;
import com.my.notebook.domain.post.UpdatePostForm;
import com.my.notebook.domain.post.UpdatePostDTO;
import com.my.notebook.service.PostService;
import com.my.notebook.service.SeqService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
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
    public String createPostForm(@PathVariable long containerId){
        return "redirect:/main/" + containerId;
    }

    @PostMapping("/createPost")
    public String createPost(@Validated @ModelAttribute CreatePostForm createPostForm,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal CustomUser user,
                             RedirectAttributes redirectAttributes){

        // Validation
        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);

            redirectAttributes.addAttribute("isCreatePost", false);
            return "redirect:/main/" + createPostForm.getContainerId();
        }

        ACIdsDTO acIdsDTO = new ACIdsDTO(user.getAccountId(), createPostForm.getContainerId());
        long postId = seqService.getPostIdSeqCurrval(acIdsDTO);

        // 포스트 생성
        CreatePostDTO createPostDTO = new CreatePostDTO(
                user.getAccountId(),
                createPostForm.getContainerId(),
                createPostForm.getPostTitle(),
                createPostForm.getPostContent()
        );
        boolean isCreatePost = postService.createPostByCreatePostDTO(createPostDTO);

        // 루팅
        redirectAttributes.addAttribute("isCreatePost", isCreatePost);
        if (!isCreatePost){
            return "redirect:/main/" + createPostForm.getContainerId();
        }

        return "redirect:/post/" + createPostForm.getContainerId() + "/" + postId;
    }

    @PostMapping("/updatePost")
    public String updatePost(@Validated @ModelAttribute UpdatePostForm updatePostForm,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal CustomUser user,
                             RedirectAttributes redirectAttributes,
                             Model model){

        log.info("{}, {}, {}, {}", updatePostForm.getPostId(), updatePostForm.getPostId(), updatePostForm.getPostTitle(), updatePostForm.getPostContent());
        if (bindingResult.hasErrors()){
            log.info("update errors={}", bindingResult);

            ACIdsDTO acIdsDTO = new ACIdsDTO(user.getAccountId(), updatePostForm.getContainerId());
            List<PostDTO> posts = postService.selectPostsByACIds(acIdsDTO);

            model.addAttribute("posts", posts);
            model.addAttribute(updatePostForm);

            return "post";
        }

        UpdatePostDTO updatePostDTO = new UpdatePostDTO(
                user.getAccountId(),
                updatePostForm.getContainerId(),
                updatePostForm.getPostId(),
                updatePostForm.getPostTitle(),
                updatePostForm.getPostContent()
        );
        boolean isUpdatePost = postService.updatePostByUpdatePostDTO(updatePostDTO);

        log.info("isUpdatePost : {}", isUpdatePost);
        log.info("포스트 수정");

        redirectAttributes.addAttribute("isUpdatePost", isUpdatePost);
        if (!isUpdatePost){
            return "redirect:/post/" + updatePostDTO.getContainerId() + "/" + updatePostDTO.getPostId();
        }
        return "redirect:/main/" + updatePostDTO.getContainerId();
    }

    @PostMapping("/deletePost")
    public String deletePost(@AuthenticationPrincipal CustomUser user,
                             @ModelAttribute ACPIdsDTO acpIdsDTO,
                             RedirectAttributes redirectAttributes){
        acpIdsDTO.setAccountId(user.getAccountId());
        boolean isDeletePost = postService.deletePostByACPIdsDTO(acpIdsDTO);

        redirectAttributes.addAttribute("isDeletePost", isDeletePost);
        return "redirect:/main/" + acpIdsDTO.getContainerId();
    }

    @GetMapping("/{containerId}/{postId}")
    public String postPage(@PathVariable("containerId") long containerId,
                           @PathVariable("postId") long postId,
                           @AuthenticationPrincipal CustomUser user,
                           Model model){

        ACPIdsDTO acpIdsDTO = new ACPIdsDTO();
        acpIdsDTO.setAccountId(user.getAccountId());
        acpIdsDTO.setContainerId(containerId);
        acpIdsDTO.setPostId(postId);

        PostDTO currentPost = postService.selectPostByACPIds(acpIdsDTO);
        UpdatePostForm updatePostForm = new UpdatePostForm(containerId, postId, currentPost.getP_title(), currentPost.getP_content());

        ACIdsDTO acIdsDTO = new ACIdsDTO(user.getAccountId(), containerId);
        List<PostDTO> posts = postService.selectPostsByACIds(acIdsDTO);

        model.addAttribute("posts", posts);
//        model.addAttribute("acIds", acIdsDTO);
        model.addAttribute("updatePostForm", updatePostForm);
        return "post";
    }
}
