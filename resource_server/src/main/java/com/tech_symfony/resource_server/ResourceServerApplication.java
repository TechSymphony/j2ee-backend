package com.tech_symfony.resource_server;

import com.tech_symfony.resource_server.api.donation.DonationClientController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SpringBootApplication
public class ResourceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
		System.out.println(linkTo(methodOn(DonationClientController.class).pay(1)).toString());
	}
}
