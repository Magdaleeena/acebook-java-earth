package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UsersController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @ModelAttribute("user")
    public User addLoggedInUserToModel(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            String email = (String) oidcUser.getAttributes().get("email");
            return userRepository.findUserByUsername(email).orElse(null);
        }
        return null;
    }

    @GetMapping("/users/after-login")
    public RedirectView afterLogin(HttpSession session) {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = (String) principal.getAttributes().get("email");
        StringBuilder displayNameCon = new StringBuilder();
        if (username != null && username.contains("@")) {
            for (int i = 0; i < username.length(); i++) {
                char c = username.charAt(i);
                if (c == '@') break;
                displayNameCon.append(c);
            }
        }
        String displayName = displayNameCon.toString();
        User user = userRepository
                .findUserByUsername(username)
                .orElseGet(() -> userRepository.save(new User(username, displayName)));
        session.setAttribute("user", user);
        return new RedirectView("/posts");
    }

    @GetMapping("/profile/{userId}")
    public ModelAndView getUserbyId(@PathVariable("userId") Long userId) {
        ModelAndView modelAndView = new ModelAndView("profile/user");
        User user = userRepository.findById(userId).orElse(null);
        Iterable<Post> posts = postRepository.findByUserId(userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("posts", posts);
        return modelAndView;
    }
}
