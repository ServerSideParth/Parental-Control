package com.luv2code.emailVerification.demo.user;

import com.luv2code.emailVerification.demo.child_registration.Child;
import com.luv2code.emailVerification.demo.registration.token.verificationToken;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User>getUsers();
    User registerUser(User theUser);
    Optional<User> findByEmail(String email);
    void saveUserVerificationToken(User theUser,String verificationToken);

    String validateToken(String token);

    verificationToken generateNewVerificationToken(String oldToken);


}
