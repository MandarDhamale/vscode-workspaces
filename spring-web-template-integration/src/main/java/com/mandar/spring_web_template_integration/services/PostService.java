package com.mandar.spring_web_template_integration.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mandar.spring_web_template_integration.models.Post;
import com.mandar.spring_web_template_integration.repositories.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            post.setCreatedAt(LocalDateTime.now());
            post.setStatus("Active");
            post.setCountry("India");
            post.setSapId("1234");
        }
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

}
