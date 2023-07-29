package com.kiterunner.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiterunner.authservice.entity.UserCredentials;

public interface UserCredentialRepository extends JpaRepository<UserCredentials, Integer>{

	Optional<UserCredentials> findByName(String username);

}
