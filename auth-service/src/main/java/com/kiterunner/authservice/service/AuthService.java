package com.kiterunner.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kiterunner.authservice.entity.UserCredentials;
import com.kiterunner.authservice.repository.UserCredentialRepository;

@Service
public class AuthService {

	@Autowired
	private UserCredentialRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	public String saveUser(UserCredentials credential) {
		credential.setPassword(passwordEncoder.encode(credential.getPassword()));
		repository.save(credential);
		return "User added to the system";
	}
	
	public String generateToken(String userName) {
		return jwtService.generateToken(userName);
	}
	
	public void validateToken(String token) {
		jwtService.validateToken(token);
	}
	
}
