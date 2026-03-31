package com.example.spring_boot_project_demo.prod_ready_features.services;

import com.example.spring_boot_project_demo.prod_ready_features.dto.PostDTO;
import com.example.spring_boot_project_demo.prod_ready_features.entities.PostEntity;
import com.example.spring_boot_project_demo.prod_ready_features.exceptions.ResourceNotFoundException;
import com.example.spring_boot_project_demo.prod_ready_features.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll()
                .stream().
                map(postEntity -> modelMapper.map(postEntity, PostDTO.class)).collect(Collectors.toList());
    }
    @Override
    public PostDTO getPostById(Long postId) {
        PostEntity postEntity =  postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post Not Found with postId"+postId));
        return modelMapper.map(postEntity, PostDTO.class);
    }
    @Override
    public PostDTO createPost(PostDTO post) {
        PostEntity postEntity = modelMapper.map(post, PostEntity.class);
        return modelMapper.map(postRepository.save(postEntity),PostDTO.class);
    }
    @Override
    public PostDTO updatePost(Long postId, PostDTO post) {
        PostEntity olderPost = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post Not Found with postId"+postId));
        post.setId(postId);
        modelMapper.map(post,olderPost);
        PostEntity savedPostEntity = postRepository.save(olderPost);
        return modelMapper.map(savedPostEntity,PostDTO.class);

    }
}
