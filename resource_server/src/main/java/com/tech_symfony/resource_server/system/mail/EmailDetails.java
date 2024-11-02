package com.tech_symfony.resource_server.system.mail;

import lombok.Builder;
import lombok.ToString;

@Builder
public record EmailDetails (String to, String subject, String body) {
}
