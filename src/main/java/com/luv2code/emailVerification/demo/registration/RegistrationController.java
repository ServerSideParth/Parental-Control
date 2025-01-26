package com.luv2code.emailVerification.demo.registration;

import com.luv2code.emailVerification.demo.child_registration.Child;
import com.luv2code.emailVerification.demo.user.UserRepository;
import com.luv2code.emailVerification.demo.child_registration.ChildRepository;
import com.luv2code.emailVerification.demo.child_registration.IChildService;
import com.luv2code.emailVerification.demo.event.RegistrationCompleteEvent;
import com.luv2code.emailVerification.demo.exception.UserAlreadyExistsException;
import com.luv2code.emailVerification.demo.exception.UserNotExistsException;
import com.luv2code.emailVerification.demo.listener.RegistrationCompleteEventListener;
import com.luv2code.emailVerification.demo.registration.token.verificationToken;
import com.luv2code.emailVerification.demo.registration.token.verificationTokenRepository;

import com.luv2code.emailVerification.demo.user.IUserService;
import com.luv2code.emailVerification.demo.user.User;
import com.luv2code.emailVerification.demo.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.Banner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final verificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RegistrationCompleteEventListener eventListener;
    private final HttpServletRequest request;
    private final ChildRepository childRepository;


     @InitBinder
     public void initBinder(WebDataBinder dataBinder){
         StringTrimmerEditor stringTrimmerEditor=new StringTrimmerEditor(true);
         dataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
     }
    @GetMapping("/showparentform")
    public String showForm(Model model,@RequestParam("login")Integer value) {
            User theUser = new User();
            model.addAttribute("user", theUser);
      if(value==0){
          model.addAttribute("condition",true);
      }

        return "Register-form";
    }
    @GetMapping("/showparentloginform")
    public String showLoginForm(Model model,@RequestParam("register")Integer value){
         if(value==1){
             model.addAttribute("condition",true);
         }
        model.addAttribute("user",new User());

        return "loginform";
    }

    @PostMapping("/processform")
    public String registerUser(@Valid @ModelAttribute("user") User theUser,BindingResult theBindingResult,
                               final HttpServletRequest request) {


        if(theBindingResult.hasErrors()){
            return "Register-form";
        }

        User user = userService.registerUser(theUser);


//        publish registration event it will send the email of user
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "sentemail";
    }

    @PostMapping("/processloginform")
    public String processLoginForm(@RequestParam("email") String email){

        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotExistsException("User is not registered");
        } else if (user.isPresent()) {
            User registeredUser = user.get();
            if(registeredUser.isEnabled()==false){
                publisher.publishEvent(new RegistrationCompleteEvent(registeredUser, applicationUrl(request)));
                return "sentemail";
            }
        }
        return "instructions";
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleSignUpException(UserAlreadyExistsException exception){
        return "redirect:/register/showparentloginform?register=1";

    }
    @ExceptionHandler(UserNotExistsException.class)
    public String handleLoginException(UserNotExistsException exception){
         return "redirect:/register/showparentform?login=0";

    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token,Model model) {
        String url = applicationUrl(request) + "/register/resend-verification-token?token=" + token;
        var theToken = tokenRepository.findByToken(token);
//        If the user is already enabled there is no need too check
        if (theToken.getUser().isEnabled()) {
            return "This account is already being verified please log in";
        }
//        If it is not enabled then we have to validate the token first it is valid or not
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return "redirect:/child/showchildform?token=" + token;


        }
        model.addAttribute("url",url);
        return "token-request";
//        return "Invalid verification link, <a href=\"" + url + "\"> Get a new verification link. </a>";
    }

    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam("token") String oldToken, final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        verificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        resendVerificationTokenEmail(applicationUrl(request), verificationToken);
        return "sentemail";

    }


    @GetMapping("/givechild")
    public String giveChild(Model model){
         String email="mishrapratham232@gmail.com";
         Optional<User> user=userService.findByEmail(email);
         User registeredUser=user.get();
        List<Child>children=registeredUser.getChildren();
        model.addAttribute("list",children);
        return "page";
    }
    private void resendVerificationTokenEmail(String applicationUrl, verificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl + "/register/verifyEmail?token=" + verificationToken.getToken();
        eventListener.sendVerificationEmail(url);

    }

    public String applicationUrl(HttpServletRequest request) {

        return "http://" + "localhost" + ":" + request.getServerPort() + request.getContextPath();
    }

}
