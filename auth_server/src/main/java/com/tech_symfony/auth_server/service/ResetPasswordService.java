package com.tech_symfony.auth_server.service;

import com.tech_symfony.auth_server.model.ResetPasswordToken;
import com.tech_symfony.auth_server.model.User;
import com.tech_symfony.auth_server.repository.ResetPasswordTokenRepository;
import com.tech_symfony.auth_server.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import system.mail.EmailService;

import java.util.UUID;


public interface ResetPasswordService {
    String resetPassword(Model model, String username);
}

@Service
@RequiredArgsConstructor
class DefaultResetPasswordService implements ResetPasswordService {

    private static final Logger log = LoggerFactory.getLogger(DefaultResetPasswordService.class);
    private final UserRepository userRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    // private final EmailService emailService;

    @Override
    public String resetPassword(Model model, String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.info("Không tìm thấy người dùng");
            model.addAttribute("error", "Tên người dùng không tìm thấy.");
            return "reset-password";
        }

        String token = UUID.randomUUID().toString();

        log.info("token: " + token);
        // emailService.sendEmail(user.getEmail(), "Khởi tạo mật khẩu", "Nhấp vào <a href=\"http://localhost:8080/reset-password?token=" + token + "\">đây</a> để khởi tạo mật khẩu ngẫu nhiên.");
        model.addAttribute("success", "Gửi email khởi tạo mật khẩu thành công!");

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setUser(user);
        resetPasswordToken.setToken(token);
        resetPasswordTokenRepository.save(resetPasswordToken);

        return "reset-password";
    }
}
