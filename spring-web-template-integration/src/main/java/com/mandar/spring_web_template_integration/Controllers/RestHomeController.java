package com.mandar.spring_web_template_integration.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import com.mandar.spring_web_template_integration.models.Post;
import com.mandar.spring_web_template_integration.services.PostService;

@RestController
@RequestMapping("/api/v1")
public class RestHomeController {

    @Autowired
    PostService postService;

    Logger logger = LoggerFactory.getLogger(RestHomeController.class);  


    @GetMapping("")
    public List<Post> home() {
        logger.error("Test Error!!");
        return postService.getAll();
    }

}
