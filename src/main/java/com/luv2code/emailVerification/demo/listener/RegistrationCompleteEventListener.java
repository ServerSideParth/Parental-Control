package com.luv2code.emailVerification.demo.listener;

import com.luv2code.emailVerification.demo.event.RegistrationCompleteEvent;
import com.luv2code.emailVerification.demo.user.IUserService;
import com.luv2code.emailVerification.demo.user.User;
import com.luv2code.emailVerification.demo.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/*
  In order to send an email once the our custom registration complete event has been published
  we have to create listener class that actually listen to this event publication so that the
  listener is going to bee the one who actually send the email to the user.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final IUserService userService;
    private final JavaMailSender mailSender;
    private  User theUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

//   step1 ->    Get the new registered user
         theUser=event.getUser();
//   step2  ->    Create a verification token for the user
        String verificationToken= UUID.randomUUID().toString();
//    step3  ->   Save the verification token
        userService.saveUserVerificationToken(theUser,verificationToken);
//    step4  ->  Build the verification url to be sent to the user
        String url=event.getApplicationurl()+"/register/verifyEmail?token="+verificationToken;
//     step5 ->  Send the email
        try {
            sendVerificationEmail(url);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the email to verify your registration : {}",url);

    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject="Email verification";
        String senderName="Parental Control Web Application";
        String content = "<p> Hi, " + theUser.getUserName() + ", </p>" +
                "<p> Thank you for registering with us.</p>" +
                "<p> Please, follow the link below to complete your registration:</p>" +
                "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                "<p> Thank you <br> Parental Control Web App Service</p>";

        MimeMessage message=mailSender.createMimeMessage();
        var messageHelper=new MimeMessageHelper(message);
        messageHelper.setFrom("mishrapratham525@gmail.com",senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(content,true);
        mailSender.send(message);


    }
}
