package com.revature.PollingApp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.PollingApp.payload.JwtAuthenticationResponse;
import com.revature.PollingApp.payload.LoginRequest;
import com.revature.PollingApp.repository.RoleRepository;
import com.revature.PollingApp.repository.UserRepository;
import com.revature.PollingApp.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@PostMapping("/siginin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
				);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtTokenProvider.generateToken(authentication);
		
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
		
		
	}

}
