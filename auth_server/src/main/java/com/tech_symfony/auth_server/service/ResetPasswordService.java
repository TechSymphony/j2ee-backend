package com.tech_symfony.auth_server.service;

import com.tech_symfony.auth_server.mail.EmailService;
import com.tech_symfony.auth_server.model.ResetPasswordToken;
import com.tech_symfony.auth_server.model.User;
import com.tech_symfony.auth_server.repository.ResetPasswordTokenRepository;
import com.tech_symfony.auth_server.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public interface ResetPasswordService {
    String resetPassword(Model model, String username);
    void reset(Model model, String token);
}

@Service
@RequiredArgsConstructor
class DefaultResetPasswordService implements ResetPasswordService {

    private static final Logger log = LoggerFactory.getLogger(DefaultResetPasswordService.class);
    private final UserRepository userRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final EmailService emailService;
    private final HttpServletRequest httpServletRequest;
    private final RandomPasswordService randomPasswordService;
    private final PasswordEncoder passwordEncoder;

    private String getFullDomain(HttpServletRequest request) {
        String scheme = request.getScheme(); // http hoặc https
        String serverName = request.getServerName(); // example.com
        int serverPort = request.getServerPort(); // 8080

        // Ghép chuỗi để tạo domain đầy đủ
        String fullDomain = scheme + "://" + serverName;

        // Chỉ thêm port nếu không phải port mặc định (80 cho http, 443 cho https)
        if ((scheme.equals("http") && serverPort != 80) ||
                (scheme.equals("https") && serverPort != 443)) {
            fullDomain += ":" + serverPort;
        }

        return fullDomain;
    }

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
        emailService.sendEmail(user.getEmail(), "Khởi tạo mật khẩu", "Nhấp vào <a href=\"" +
                getFullDomain(httpServletRequest) +
                "/reset/" + token + "\">đây</a> để khởi tạo mật khẩu ngẫu nhiên.");

        model.addAttribute("success", "Gửi email khởi tạo mật khẩu thành công!");

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setUser(user);
        resetPasswordToken.setToken(token);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 30);
        resetPasswordToken.setExpiryDate(calendar.getTime());

        resetPasswordTokenRepository.save(resetPasswordToken);

        return "reset-password";
    }

    @Override
    public void reset(Model model, String token) {
        String result = this.validatePasswordResetToken(token);

        if(result != null) {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            return;
        }

        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken(token);
        if(resetPasswordToken != null)
        {
            User user = resetPasswordToken.getUser();
            String newPassword = randomPasswordService.generatePassword();
            emailService.sendEmail(user.getEmail(), "Mật khẩu mới", "Mật khẩu mới của bạn là: " + newPassword);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
        model.addAttribute("success", "Đã đặt lại mật khẩu thành công, mật khẩu mới đã được gửi qua email!");
    }

    private String validatePasswordResetToken(String token) {
        final ResetPasswordToken passToken = resetPasswordTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(ResetPasswordToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(ResetPasswordToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
