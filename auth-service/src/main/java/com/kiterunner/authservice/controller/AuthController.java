package com.kiterunner.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiterunner.authservice.dto.AuthRequest;
import com.kiterunner.authservice.dto.JwtResponse;
import com.kiterunner.authservice.dto.RefreshTokenRequest;
import com.kiterunner.authservice.entity.RefreshToken;
import com.kiterunner.authservice.entity.UserCredentials;
import com.kiterunner.authservice.service.AuthService;
import com.kiterunner.authservice.service.RefreshTokenService;


@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService service;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@PostMapping("/register")
	public String addNewUser(@RequestBody UserCredentials user) {
		return service.saveUser(user);
	}
	
	@PostMapping("/token")
	public JwtResponse getToken(@RequestBody AuthRequest authRequest) {
		Authentication authenticate =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		if(authenticate.isAuthenticated()) {
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUserName());
			//service.generateToken(authRequest.getUserName());
			return JwtResponse.builder()
				.accessToken(service.generateToken(authRequest.getUserName()))
				.token(refreshToken.getToken())
				.build();
		}else
			throw new UsernameNotFoundException("Invalid User Request");
	}
	
	@PostMapping("/refreshToken")
	public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		return refreshTokenService.findByToken(refreshTokenRequest.getToken())
			.map(refreshTokenService::verifyExpiration)
			.map(RefreshToken::getUserCredentials)
			.map(userCredential -> {
				String accessToken = service.generateToken(userCredential.getName());
				return JwtResponse.builder()
						.accessToken(accessToken)
						.token(refreshTokenRequest.getToken()).build();
			}).orElseThrow(()->new RuntimeException("RefreshToken Not in DB"));
	}
	
	@GetMapping("/validate")
	public String getToken(String token) {
		service.validateToken(token);
		return "Token is valid";
	}
}
