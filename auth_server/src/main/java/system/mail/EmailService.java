package system.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {


	private final JavaMailSender mailSender;

	private final TemplateEngine templateEngine;

	public void sendEmail(String to, String subject, String body) {

		EmailDetails emailDetails = EmailDetails.builder()
				.to(to)
				.subject(subject)
				.body(body)
				.build();
		log.info(emailDetails.toString());
		handleQueueEmail(emailDetails);

	}

	public void handleQueueEmail(EmailDetails emailDetails) {

		try {
//			SimpleMailMessage message = new SimpleMailMessage();
//			message.setFrom(emailSender);
//			message.setTo(emailDetails.to());
//			message.setSubject(emailDetails.subject());
//			message.setText(emailDetails.body());
//			mailSender.send(message);


			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			// Cài đặt thông tin email
			helper.setTo(emailDetails.to());
			helper.setSubject(emailDetails.subject());

			// Tạo context của Thymeleaf
			Context context = new Context();
			context.setVariable("to", emailDetails.to());
			context.setVariable("subject", emailDetails.subject());
			context.setVariable("body", emailDetails.body());

			// Tạo nội dung email từ template
			String htmlContent = templateEngine.process("mail.html", context);
			helper.setText(htmlContent, true);

			// Gửi email
			mailSender.send(message);

			log.info("Mail {} sent successfully", emailDetails);

		}catch (MailException | MessagingException e){
			log.debug("Sending mail failed due to some error {}", e.getMessage());
		}
	}
}
