package system.mail;

import lombok.Builder;

@Builder
public record EmailDetails (String to, String subject, String body) {
}
