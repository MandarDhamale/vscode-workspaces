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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import com.mandar.spring_web_template_integration.models.Account;
import com.mandar.spring_web_template_integration.models.Post;
import com.mandar.spring_web_template_integration.services.AccountService;
import com.mandar.spring_web_template_integration.services.PostService;
import com.sun.jdi.connect.Connector;

@RestController
@RequestMapping("/api/account/{id}")
public class RestHomeController {

    @Autowired
    PostService postService;

    @Autowired
    AccountService accountService;

    Logger logger = LoggerFactory.getLogger(RestHomeController.class);  


    @GetMapping("")
    public String home(@PathVariable Long id) {
        logger.error("Test Error!!");
        return accountService.findById(id).get().getEmail();
    }

}
