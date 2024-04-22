package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.models.*;
import com.yicem.backend.yicem.payload.request.*;
import com.yicem.backend.yicem.payload.response.JwtResponse;
import com.yicem.backend.yicem.payload.response.MessageResponse;
import com.yicem.backend.yicem.repositories.*;
import com.yicem.backend.yicem.security.jwt.JwtUtils;
import com.yicem.backend.yicem.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SellerRepository sellerRepository;

	@Autowired
	BuyerRepository buyerRepository;

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signupRequest.getUsername(),
							 signupRequest.getEmail(),
							 encoder.encode(signupRequest.getPassword()));

		Set<String> strRoles = signupRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);

						user.setRoles(roles);
						userRepository.save(user);

						break;
					case "seller":
						Role sellerRole = roleRepository.findByName(ERole.ROLE_SELLER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(sellerRole);

						user.setRoles(roles);
						userRepository.save(user);

						Seller seller = new Seller(user.getId(), signupRequest.getUsername(), signupRequest.isApproved(),
								signupRequest.getAddress(), signupRequest.getPhone(), signupRequest.getBusinessName(),
								signupRequest.getOpeningHour(), signupRequest.getClosingHour(),
								signupRequest.getLocationLatitude(), signupRequest.getLocationLongitude(),
								signupRequest.getReservationTimeout());
						sellerRepository.save(seller);

						break;
					case "buyer":
						Role buyerRole = roleRepository.findByName(ERole.ROLE_BUYER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(buyerRole);

						user.setRoles(roles);
						userRepository.save(user);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);

						user.setRoles(roles);
						userRepository.save(user);
				}
			});
		}

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/signup/seller")
	public ResponseEntity<?> registerSeller(@Valid @RequestBody SellerSignupRequest signupRequest) {
		if (sellerRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signupRequest.getUsername(),
				signupRequest.getEmail(),
				encoder.encode(signupRequest.getPassword()));

		Set<Role> roles = new HashSet<>();

		Role sellerRole = roleRepository.findByName(ERole.ROLE_SELLER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(sellerRole);
		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);

		user.setRoles(roles);
		userRepository.save(user);

		Seller seller = new Seller(user.getId(), signupRequest.getUsername(), signupRequest.isApproved(),
				signupRequest.getAddress(), signupRequest.getPhone(), signupRequest.getBusinessName(),
				signupRequest.getOpeningHour(), signupRequest.getClosingHour(), signupRequest.getLocationLatitude(),
				signupRequest.getLocationLongitude(), signupRequest.getReservationTimeout());
		sellerRepository.save(seller);

		return ResponseEntity.ok(new MessageResponse("Seller registered successfully!"));
	}

	@PostMapping("/signup/buyer")
	public ResponseEntity<?> registerBuyer(@Valid @RequestBody BuyerSignupRequest signupRequest) {
		if (buyerRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signupRequest.getUsername(),
				signupRequest.getEmail(),
				encoder.encode(signupRequest.getPassword()));

		Set<Role> roles = new HashSet<>();

		Role buyerRole = roleRepository.findByName(ERole.ROLE_BUYER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(buyerRole);
		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);

		user.setRoles(roles);
		userRepository.save(user);

		Buyer buyer = new Buyer(user.getId(), signupRequest.getUsername());
		buyerRepository.save(buyer);

		return ResponseEntity.ok(new MessageResponse("Buyer registered successfully!"));
	}

	@PostMapping("/signup/admin")
	public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminSignupRequest signupRequest) {
		if (adminRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signupRequest.getUsername(),
				signupRequest.getEmail(),
				encoder.encode(signupRequest.getPassword()));

		Set<Role> roles = new HashSet<>();

		Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(adminRole);
		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);

		user.setRoles(roles);
		userRepository.save(user);

		Admin admin = new Admin(user.getId(), signupRequest.getUsername());
		adminRepository.save(admin);

		return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
	}

}
