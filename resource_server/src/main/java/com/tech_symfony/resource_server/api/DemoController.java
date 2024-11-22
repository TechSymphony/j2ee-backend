package com.tech_symfony.resource_server.api;


import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.api.donation.DonationService;
import com.tech_symfony.resource_server.api.notification.Notification;
import com.tech_symfony.resource_server.api.notification.NotificationService;
import com.tech_symfony.resource_server.api.user.UserRepository;
import com.tech_symfony.resource_server.api.user.UserService;
import com.tech_symfony.resource_server.system.mail.EmailService;
import com.tech_symfony.resource_server.system.payment.vnpay.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/demo")
public class DemoController {
	private final EmailService emailService;
	private  final DonationService donationService;
	private final NotificationService notificationService;

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

		donationService.sendEventVerify(42);
		return "Welcome to the resource server!";
	}

	private final UserRepository userRepository;

	@GetMapping("/socket/{message}")
	public String socket(@PathVariable String message) {
		Notification notification = new Notification();
		notification.setUser(userRepository.findByUsername("admin").get());
		notification.setMessage(message);
		notificationService.sendTo(notification);
		return "Welcome to the resource server!";
	}
}
