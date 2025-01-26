package com.luv2code.emailVerification.demo.security;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class UserRegistrationSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();

    }





    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer->
                        configurer
                                .requestMatchers("/start/**").permitAll()
                                .requestMatchers("/child/**").permitAll()
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/started/**").permitAll()
                                .requestMatchers("/templates/**").permitAll()

                )
                .exceptionHandling(configurer->
                        configurer.accessDeniedPage("/child/showoffpage")
                );



        return http.build();
    }

}
