package com.mandar.spring_web_template_integration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mandar.spring_web_template_integration.models.Authority;
import com.mandar.spring_web_template_integration.repositories.AuthorityRepository;

@Service
public class AuthorityService {

    @Autowired
    AuthorityRepository authorityRepository;

    public Authority save(Authority authority){ 
        return authorityRepository.save(authority);
    }
    
}
