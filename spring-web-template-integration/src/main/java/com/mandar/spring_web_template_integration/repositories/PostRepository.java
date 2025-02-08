package com.mandar.spring_web_template_integration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mandar.spring_web_template_integration.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
}
