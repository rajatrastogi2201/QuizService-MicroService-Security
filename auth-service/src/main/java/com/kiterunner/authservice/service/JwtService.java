package com.kiterunner.authservice.service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.kiterunner.authservice.entity.UserCredentials;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET = "f6bDi3LIkDiKNyrWDgDQdbsMgzdMjPUGBgKrfSr7NvNsRDuRy80iiUEaNxxudGDc";
	
	
	public void validateToken(String token) {
		 Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		
	}
	
	public String generateToken(String userName) {
		Map<String,Object> claims = new HashMap<>();
		return createToken(claims,userName);
	}
	
	public String createToken(Map<String,Object> claims, String userName) {
		return Jwts.builder()
		.setClaims(claims)
		.setSubject(userName)
		.setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
		.signWith(getSignKey(),SignatureAlgorithm.HS256)
		.compact();

	}

	private Key getSignKey() {
		byte[] keyBytes=Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
