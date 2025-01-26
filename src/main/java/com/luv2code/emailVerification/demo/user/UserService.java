package com.luv2code.emailVerification.demo.user;
import com.luv2code.emailVerification.demo.child_registration.Child;
import com.luv2code.emailVerification.demo.exception.UserNotExistsException;
import com.luv2code.emailVerification.demo.registration.token.verificationToken;
import com.luv2code.emailVerification.demo.registration.token.verificationTokenRepository;

import com.luv2code.emailVerification.demo.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final verificationTokenRepository tokenRepository;
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(User theUser) {
        Optional<User>user=this.findByEmail(theUser.getEmail());
        if(user.isPresent()){
            throw new UserAlreadyExistsException("User with email "+theUser.getEmail()+" already exists");
        }

        var newUser=new User();
        newUser.setUserName(theUser.getUserName());
        newUser.setEmail(theUser.getEmail());
        newUser.setPassword(passwordEncoder.encode(theUser.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String Token) {
//        For saving the verification token in the database
        var verificationToken=new verificationToken(Token,theUser);
//        once the token for an appropriate user is set then we just going to call the token repository to actually save
        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String token) {
        verificationToken theToken=tokenRepository.findByToken(token);
//        if it is already expired it is removed from the database
        if(theToken==null){
            return "Invalid verification token";
        }
//        If it is found then check if it is expired the removed it from my database
        User user=theToken.getUser();
        Calendar calender= Calendar.getInstance();
        if((theToken.getExpirationTime().getTime()-calender.getTime().getTime())<=0){
//            tokenRepository.delete(theToken);
            return "Token already expired";
        }
//        if the token is not about to expired it means it's token is valid
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public verificationToken generateNewVerificationToken(String oldToken) {
        verificationToken verificationToken=tokenRepository.findByToken(oldToken);
        var verificationTokenTime=new verificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(verificationTokenTime.getTokenExpirationTime());
        return tokenRepository.save(verificationToken);
    }




}
