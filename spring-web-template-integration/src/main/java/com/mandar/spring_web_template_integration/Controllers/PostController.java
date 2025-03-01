package com.mandar.spring_web_template_integration.Controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mandar.spring_web_template_integration.models.Account;
import com.mandar.spring_web_template_integration.models.Post;
import com.mandar.spring_web_template_integration.services.AccountService;
import com.mandar.spring_web_template_integration.services.PostService;

@Controller
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    AccountService accountService;

    @GetMapping("/post/{id}")
    public String getPosts(@PathVariable Long id, Model model, Principal principal) {

        Optional<Post> optionalPost = postService.getById(id);

        String authUser = "email"; // dummy name

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);

            if (principal != null) {
                authUser = principal.getName();
            }

            if (authUser.equals(post.getAccount().getEmail())) {
                model.addAttribute("isOwner", true);
            } else {
                model.addAttribute("isOwner", false);
            }

            return "post_views/post";
        } else {
            return "404";
        }

    }

    @GetMapping("/post_browse")
    public String postBrowse(Model model) {

        List<Post> posts = postService.getAll();

        model.addAttribute("posts", posts);

        return "post_views/post_browse";

    }

    // rendering the add post page
    @GetMapping("/posts/add")
    @PreAuthorize("isAuthenticated()")
    public String addPost(Model model, Principal principal) {

        String authUser = "email";
        if (principal != null) {
            authUser = principal.getName();
        }

        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if (optionalAccount.isPresent()) {
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);

            return "post_views/post_add";

        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/post/add")
    @PreAuthorize("isAuthenticated()")
    public String addNewPost(@ModelAttribute Post post, Principal principal) {
        String authUser = "email";

        if (principal != null) {
            authUser = principal.getName();
        }

        if (post.getAccount().getEmail().compareTo(authUser) < 0) {
            return "redirect:/?error";
        }

        System.out.println(post.getId());
        postService.save(post);
        System.out.println(post.getId());
        return "redirect:/edit_post/" + post.getId();

    }

    @GetMapping("/edit_post/{id}")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model) {

        Optional<Post> optionalPost = postService.getById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_views/post_edit";
        } else {
            return "404";
        }
    }

    @PostMapping("/post/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, @ModelAttribute Model model, Post post,
            RedirectAttributes redirectAttributes, @RequestParam(required = false) String action) {

        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());
            existingPost.setStatus(post.getStatus());
            existingPost.setSapId(post.getSapId());
            existingPost.setCountry(post.getCountry());

            postService.save(existingPost);

        }

        redirectAttributes.addFlashAttribute("successMessage", " Data Updated Successfully");

        if ("save_close".equals(action)) {
            return "redirect:/post_browse"; // Redirect to post list or dashboard
        }

        return "redirect:/edit_post/" + post.getId();

    }

    @GetMapping("/delete_post/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Long id) {

        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            postService.delete(post);
            return "redirect:/post_browse";
        } else {
            return "404";
        }

    }
}
