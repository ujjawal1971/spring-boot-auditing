package com.example.spring_boot_project_demo.prod_ready_features.services;

import com.example.spring_boot_project_demo.prod_ready_features.dto.PostDTO;
import com.example.spring_boot_project_demo.prod_ready_features.entities.PostEntity;

import java.util.List;

public interface PostService {
    List<PostDTO> getAllPosts();
    PostDTO createPost(PostDTO post);

    PostDTO getPostById(Long postId);

    PostDTO updatePost(Long postId, PostDTO post);
}
