package com.kiterunner.authservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kiterunner.authservice.dto.CustomUserDetails;
import com.kiterunner.authservice.entity.UserCredentials;
import com.kiterunner.authservice.repository.UserCredentialRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserCredentialRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		Optional<UserCredentials> credential= repository.findByName(username);
		return credential.map(CustomUserDetails::new).orElseThrow(()-> new UsernameNotFoundException("user not found with name : " + username ));
		
		
	}

}
