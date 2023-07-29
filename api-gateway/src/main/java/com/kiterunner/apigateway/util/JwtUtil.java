package com.kiterunner.apigateway.util;


import java.security.Key;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

	private static final String SECRET = "f6bDi3LIkDiKNyrWDgDQdbsMgzdMjPUGBgKrfSr7NvNsRDuRy80iiUEaNxxudGDc";
	
	
	public void validateToken(String token) {
		 Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		
	}
	
	private Key getSignKey() {
		byte[] keyBytes=Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
