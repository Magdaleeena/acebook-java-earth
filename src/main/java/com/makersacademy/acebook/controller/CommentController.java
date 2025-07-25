package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.PostRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/posts/{postId}/comments")
    public RedirectView addComment(
            @PathVariable Long postId,
            @RequestParam String content,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new RedirectView("/login");
        }
        if (content == null || content.isBlank()) {
            return new RedirectView("/posts?error=emptyComment");
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        var comment = new Comment(post, user, content);
        commentRepository.save(comment);
        return new RedirectView("/posts");
    }


    @PostMapping("/comments/{commentId}/delete")
    public RedirectView deleteComment(
            @PathVariable Long commentId,
            HttpSession session
    ) {
        User currentUser = (User) session.getAttribute("user");
        commentRepository.findById(commentId).ifPresent(c -> {
            if (currentUser != null && c.getUser().getId().equals(currentUser.getId())) {
                commentRepository.deleteById(commentId);
            }
        });
        return new RedirectView("/posts");
    }

    @PostMapping("/comments/{commentId}/like")
    public RedirectView likeComment(@PathVariable Long commentId) {
        var c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        c.setLikeCount(c.getLikeCount() + 1);
        commentRepository.save(c);
        return new RedirectView("/posts");
    }

    @PostMapping("/comments/{commentId}/unlike")
    public RedirectView unlikeComment(@PathVariable Long commentId) {
        var c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (c.getLikeCount() > 0) c.setLikeCount(c.getLikeCount() - 1);
        commentRepository.save(c);
        return new RedirectView("/posts");
    }
}