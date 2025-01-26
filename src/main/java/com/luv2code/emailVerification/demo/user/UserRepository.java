package com.luv2code.emailVerification.demo.user;

import com.luv2code.emailVerification.demo.child_registration.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);


}
