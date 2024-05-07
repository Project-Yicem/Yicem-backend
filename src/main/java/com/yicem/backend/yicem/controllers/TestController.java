package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.models.User;
import com.yicem.backend.yicem.repositories.UserRepository;
import com.yicem.backend.yicem.services.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MailController mailController;

	@GetMapping("/public")
	public String publicAccess() {
		//mailController.mailTest();
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('SELLER') or hasRole('BUYER') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/seller")
	@PreAuthorize("hasRole('SELLER')")
	public String sellerAccess() {
		return "Seller Board.";
	}

	@GetMapping("/buyer")
	@PreAuthorize("hasRole('BUYER')")
	public String buyerAccess() {
		return "Buyer Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@GetMapping("/users")
	public List<User> getUsers() {
		return userRepository.findAll();
	}

}
