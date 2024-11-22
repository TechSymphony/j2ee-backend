package com.tech_symfony.resource_server.api;


import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.api.donation.DonationService;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationVerifyEventVm;
import com.tech_symfony.resource_server.system.mail.EmailDetails;
import com.tech_symfony.resource_server.api.notification.Notification;
import com.tech_symfony.resource_server.api.notification.NotificationService;
import com.tech_symfony.resource_server.api.user.UserRepository;
import com.tech_symfony.resource_server.api.user.UserService;
import com.tech_symfony.resource_server.system.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/demo")
@Slf4j
public class DemoController {
	private final EmailService emailService;
	private  final DonationService donationService;
	private final NotificationService notificationService;


	@GetMapping("/mail")
	public String mail() {
		EmailDetails emailDetails = EmailDetails.builder()
				.to("hi@email.com")
				.subject("Hello")
				.body("Test")
				.build();
		emailService.handleQueueEmail(emailDetails);
		return "Welcome to the resource server!";
	}

	@GetMapping("/payment/{id}")
	public String payment(@PathVariable Integer id) {

		Donation donation = donationService.findById(id);
		DonationVerifyEventVm donationVerifyEventVm = new DonationVerifyEventVm(id, donation.getCampaign().getId());
		donationService.verify(donationVerifyEventVm);

		return "Welcome to the resource server! ";
	}
	@GetMapping("/payment/{id}/event")
	public String paymentEvent(@PathVariable Integer id) {

		Donation donation = donationService.findById(id);
		DonationVerifyEventVm donationVerifyEventVm = new DonationVerifyEventVm(id, donation.getCampaign().getId());

		donationService.sendEventVerify(donationVerifyEventVm);

		return "Test payment event!";
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
