package com.luv2code.emailVerification.demo.security;

import com.luv2code.emailVerification.demo.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserRegistrationDetails implements UserDetails {
    private String userName;
    private String password;
/*    when the user registered its going to be disabled but after the email verification is going to be enabled so that
      user can actually log right
 */
    private boolean isEnabled;
    private List<GrantedAuthority> authorities;

    public UserRegistrationDetails(User user) {
        this.userName =user.getEmail() ;
        this.password = user.getPassword();
        this.isEnabled = user.isEnabled();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
