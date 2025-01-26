package com.luv2code.emailVerification.demo.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import com.luv2code.emailVerification.demo.registration.token.verificationToken;
import com.luv2code.emailVerification.demo.child_registration.Child;

import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parent")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    @NotNull(message = "\uD83D\uDEA8 Oops! This field is required")
    private String userName;

    @NaturalId(mutable = true)
    @Column(name = "email")
    @NotNull(message = "\uD83D\uDEA8 Oops! This field is required")
    private String email;

    @Column(name = "password")
    @NotNull(message = "\uD83D\uDEA8 Oops! This field is required")
    @Size(min = 6,message = "\uD83D\uDD11 Your password must be at least 6 characters long for security")
    private String password;

    @OneToOne(mappedBy = "user")
    private verificationToken token;
//    cascade = {CascadeType.REMOVE}

    @OneToMany(mappedBy = "parent")
    private List<Child>children;
    @Column(name = "is_enabled")
//    cascade = {CascadeType.REMOVE}
    private boolean isEnabled=false;
}
