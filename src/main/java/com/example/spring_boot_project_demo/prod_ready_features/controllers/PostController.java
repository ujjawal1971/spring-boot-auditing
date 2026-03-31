package com.example.spring_boot_project_demo.prod_ready_features.controllers;


import com.example.spring_boot_project_demo.prod_ready_features.dto.PostDTO;
import com.example.spring_boot_project_demo.prod_ready_features.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostDTO> getPosts() {
        return postService.getAllPosts();
    }
    @GetMapping("/{postId}")
    public PostDTO getPostById(@PathVariable("postId") Long postId) {
        return postService.getPostById(postId);
    }
    @PostMapping
    public PostDTO createPost(@RequestBody PostDTO post) {
        return postService.createPost(post);
    }
    @PutMapping("/{PostId}")
    public PostDTO updatePost(@PathVariable("PostId") Long postId, @RequestBody PostDTO post) {
        return postService.updatePost(postId,post);
    }
}
