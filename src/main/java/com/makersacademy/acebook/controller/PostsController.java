package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class PostsController {

    @Autowired
    PostRepository repository;

    @GetMapping("/posts")
    public String index(Model model) {
        Iterable<Post> posts = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        return "posts/index";
    }

    @PostMapping("/posts")
    public RedirectView create(@ModelAttribute Post post) {
        if (post.getContent() == null|| post.getContent().isEmpty()) {
            return new RedirectView("/posts?error=emptyContent");
        }
        if (post.getContent().matches(".*(https?://|www\\.).*")) {
            return new RedirectView("/posts?error=noUrl");
        }
        repository.save(post);
        return new RedirectView("/posts");
    }

    @PostMapping("/posts/{id}/like")
    public RedirectView likePost(@PathVariable Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setLikeCount(post.getLikeCount() + 1);
        repository.save(post);

        return new RedirectView("/posts");
    }

    @PostMapping("/posts/{id}/unlike")
    public RedirectView unlikePost(@PathVariable Long id) {
        Post post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getLikeCount() > 0) {
            post.setLikeCount(post.getLikeCount() - 1);
            repository.save(post);
        }
//        should we have error handling here? what happens if the like count is zero?

        return new RedirectView("/posts");
    }

}
