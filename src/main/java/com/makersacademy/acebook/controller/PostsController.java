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
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public String listPosts(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("currentUser", currentUser);

        Iterable<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        model.addAttribute("comment", new Comment());

        Map<Long, List<Comment>> postComments = new HashMap<>();
        for (Post post : posts) {
            List<Comment> comments = commentRepository.findByPostOrderByCreatedAtAsc(post);
            postComments.put(post.getId(), comments);
        }
        model.addAttribute("postComments", postComments);

        return "posts/index";
    }

    @PostMapping
    public RedirectView createPost(@ModelAttribute Post post, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new RedirectView("/login");
        }
        String content = post.getContent();
        if (content == null || content.isBlank()) {
            return new RedirectView("/posts?error=emptyContent");
        }
        if (content.matches(".*(https?://|www\\.).*")) {
            return new RedirectView("/posts?error=noUrl");
        }
        post.setUser(user);
        postRepository.save(post);
        return new RedirectView("/posts");
    }

    @PostMapping("/{postId}/like")
    public RedirectView likePost(@PathVariable Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
        return new RedirectView("/posts");
    }

    @PostMapping("/{postId}/unlike")
    public RedirectView unlikePost(@PathVariable Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        int count = post.getLikeCount();
        if (count > 0) post.setLikeCount(count - 1);
        postRepository.save(post);
        return new RedirectView("/posts");
    }
}

