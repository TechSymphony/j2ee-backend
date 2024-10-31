package com.tech_symfony.resource_server.api;


import com.tech_symfony.resource_server.system.mail.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/demo")
public class DemoController {
	private final EmailService emailService;

	@GetMapping("/mail")
	public String mail() {
		emailService.sendEmail(
				"hi@email.com",
				"Hello",
				"Test"
		);
		return "Welcome to the resource server!";
	}

	@GetMapping("/payment")
	public String payment() {
		emailService.sendEmail(
				"hi@email.com",
				"Hello",
				"Test"
		);
		return "Welcome to the resource server!";
	}

}
