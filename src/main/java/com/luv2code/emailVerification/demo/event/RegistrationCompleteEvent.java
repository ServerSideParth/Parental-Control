package com.luv2code.emailVerification.demo.event;

import com.luv2code.emailVerification.demo.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;
/*    This is the url that we are going to build to send along to user email
     so when user click on this url they should be able to verify their emails so they
     can login to the systems
 */
    private String applicationurl;

    public RegistrationCompleteEvent(User user, String applicationurl) {
        super(user);
        this.user = user;
        this.applicationurl = applicationurl;
    }
}
