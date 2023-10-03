package com.blogapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.model.User;
import com.blogapp.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
	
	Optional<VerificationToken> findByToken(String token);
	
	Optional<VerificationToken> findByUser(User user);

}
