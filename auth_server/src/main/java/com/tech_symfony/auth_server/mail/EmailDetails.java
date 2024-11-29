package com.tech_symfony.auth_server.mail;

import lombok.Builder;

@Builder
public record EmailDetails (String to, String subject, String body) {
}
