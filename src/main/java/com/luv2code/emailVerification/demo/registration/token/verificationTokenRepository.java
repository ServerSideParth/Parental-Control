package com.luv2code.emailVerification.demo.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface verificationTokenRepository extends JpaRepository<verificationToken,Long> {
    verificationToken findByToken(String token);
}
