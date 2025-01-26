package com.luv2code.emailVerification.demo.registration.token;

import com.luv2code.emailVerification.demo.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import java.util.Calendar;
import java.util.Date;
/*
 This token is generally generated for an user we need to tied the user with token through
 one-one relationship
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "token")
public class verificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "token")
    private String token;
    @Column(name = "expiration_time")
    private Date expirationTime;
    @OneToOne
    @JoinColumn(name = "parent_id")
    private User user;

    public verificationToken(String token) {
        super();
        this.token=token;
        this.expirationTime=this.getTokenExpirationTime();
    }

    public verificationToken(String token,User user) {
        super();
        this.token=token;
        this.user = user;
        this.expirationTime=this.getTokenExpirationTime();
    }


    public Date getTokenExpirationTime(){
        Calendar calendar=Calendar.getInstance();
//        we are going to get the actual time this token will be generated in our system
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,1);
        return new Date(calendar.getTime().getTime());
    }
}






















