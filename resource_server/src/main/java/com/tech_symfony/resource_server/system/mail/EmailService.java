package com.tech_symfony.resource_server.system.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {


	private final RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.exchange.email.name}")
	private String emailExchange;

	@Value("${rabbitmq.binding.email.name}")
	private String emailRoutingKey;


	@Value("${spring.mail.username}")
	private String emailSender;

	private final JavaMailSender mailSender;

	public void sendEmail(String to, String subject, String body) {

		rabbitTemplate.convertAndSend(emailExchange,
				emailRoutingKey,
				EmailDetails.builder()
						.to(to)
						.subject(subject)
						.body(body)
						.build());
	}

	@RabbitListener(queues = "email_queue")
	public void handleQueueEmail(EmailDetails emailDetails) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(emailSender);
			message.setTo(emailDetails.to());
			message.setSubject(emailDetails.subject());
			message.setText(emailDetails.body());
			mailSender.send(message);
			log.info("Mail sent successfully");
		}catch (MailException e){
			log.debug("Sending mail failed due to some error");
		}
	}
}
