package com.tech_symfony.auth_server.controller;

import com.tech_symfony.auth_server.model.User;
import com.tech_symfony.auth_server.repository.UserRepository;
import com.tech_symfony.auth_server.service.ResetPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private final UserRepository userRepository;
    private final ResetPasswordService resetPasswordService;

    @GetMapping("/reset-password")
    String resetPassword() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    String resetPasswordPost(@RequestParam String username, Model model) {
        return resetPasswordService.resetPassword(model, username);
    }
}
