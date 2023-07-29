package com.kiterunner.authservice.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kiterunner.authservice.entity.RefreshToken;
import com.kiterunner.authservice.repository.RefreshTokenRepository;
import com.kiterunner.authservice.repository.UserCredentialRepository;

@Service
public class RefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Autowired
	private UserCredentialRepository credentialRepository;
	
	public RefreshToken createRefreshToken(String userName){
		RefreshToken refreshToken = RefreshToken.builder()
			.userCredentials(credentialRepository.findByName(userName).get())
			.token(UUID.randomUUID().toString())
			.expiryDate(Instant.now().plusMillis(1000*60*10))
			.build();
		
		return refreshTokenRepository.save(refreshToken);
	}
	
	public RefreshToken verifyExpiration(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now())<0) {
			refreshTokenRepository.delete(token);
			throw new RuntimeException(token.getToken() + "Refresh token was expired");
		}
		return token;
	}
	public Optional<RefreshToken> findByToken(String token){
		return refreshTokenRepository.findByToken(token);
	}
	
}
