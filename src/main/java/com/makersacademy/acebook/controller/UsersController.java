package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Optional;

@RestController
public class UsersController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users/after-login")
    public RedirectView afterLogin() {
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
        userRepository
                .findUserByUsername(username)
                .orElseGet(() -> userRepository.save(new User(username, displayName)));
        return new RedirectView("/posts");
    }

    @GetMapping("/profile/{userId}")
    public ModelAndView getUserbyId(@PathVariable("userId") Long userId) {
        ModelAndView modelAndView = new ModelAndView("/profile/user");
        User user = userRepository.findById(userId).orElse(null);
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
