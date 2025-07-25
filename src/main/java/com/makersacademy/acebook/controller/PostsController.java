package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.PostRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostsController {

    @Autowired
    PostRepository repository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/posts")
    public String index(Model model, HttpSession session) {
        Iterable<Post> posts = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        Map<Long, List<Comment>> postComments = new HashMap<>();
        for (Post post : posts) {
            List<Comment> sortedComments = commentRepository.findByPostOrderByCreatedAtAscIdAsc(post);
            postComments.put(post.getId(), sortedComments);
        }
        model.addAttribute("postComments", postComments);
        model.addAttribute("comment", new Comment());
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "posts/index";
    }

    @PostMapping("/posts")
    public RedirectView create(@ModelAttribute Post post, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.out.println("User is NULL in POST /posts");
            return new RedirectView("/login");
        }
        if (post.getContent() == null|| post.getContent().isEmpty()) {
            return new RedirectView("/posts?error=emptyContent");
        }
        if (post.getContent().matches(".*(https?://|www\\.).*")) {
            return new RedirectView("/posts?error=noUrl");
        }
        post.setUser(user);
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
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        if (post.getLikeCount() > 0) {
            post.setLikeCount(post.getLikeCount() - 1);
            repository.save(post);
        }
        return new RedirectView("/posts");
    }
}








