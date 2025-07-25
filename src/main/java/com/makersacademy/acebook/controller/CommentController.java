package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.CommentRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/posts/{postId}/comments")
    public RedirectView addComment(
            @PathVariable Long postId,
            @RequestParam String content,
            @AuthenticationPrincipal OidcUser principal) {
        if (content.trim().isEmpty()) {
            return new RedirectView("/posts?error=emptyComment");
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        String email = principal.getAttribute("email");
        User user = userRepository.findUserByUsername(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Comment comment = new Comment(post, user, content);
        commentRepository.save(comment);
        return new RedirectView("/posts");
    }

    @PostMapping("/comments/{commentId}/like")
    public RedirectView likeComment(@PathVariable Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
        return new RedirectView("/posts");
    }

    @PostMapping("/comments/{commentId}/unlike")
    public RedirectView unlikeComment(@PathVariable Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (comment.getLikeCount() > 0) {
            comment.setLikeCount(comment.getLikeCount() - 1);
            commentRepository.save(comment);
        }
        return new RedirectView("/posts");
    }
}

