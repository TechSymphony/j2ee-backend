package com.tech_symfony.auth_server.controller;

import com.tech_symfony.auth_server.model.User;
import com.tech_symfony.auth_server.repository.UserRepository;
import com.tech_symfony.auth_server.service.ResetPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private final UserRepository userRepository;
    private final ResetPasswordService resetPasswordService;

    @GetMapping("/reset-password")
    String resetPassword() {
        return "reset-password";
    }

    @GetMapping("/reset/{token}")
    RedirectView resetWithToken(@PathVariable String token, RedirectAttributes attributes, Model model) {
        attributes.addFlashAttribute("success", "Đã đặt lại mật khẩu thành công, mật khẩu mới đã được gửi qua email!");

        resetPasswordService.reset(model, token);

        return new RedirectView("/login", true);
    }


    @PostMapping("/reset-password")
    String resetPasswordPost(@RequestParam String username, Model model) {
        return resetPasswordService.resetPassword(model, username);
    }
}
