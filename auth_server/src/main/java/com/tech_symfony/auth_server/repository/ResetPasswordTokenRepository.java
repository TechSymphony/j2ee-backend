package com.tech_symfony.auth_server.repository;

import com.tech_symfony.auth_server.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Integer> {
    ResetPasswordToken findByToken(String token);
}
