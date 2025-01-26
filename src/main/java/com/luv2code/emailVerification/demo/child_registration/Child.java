package com.luv2code.emailVerification.demo.child_registration;


import com.luv2code.emailVerification.demo.user.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "child")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer child_id;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;

//(cascade = CascadeType.PERSIST)
    @Column(name = "first_name")
    @NotNull(message = "\uD83D\uDEA8 Oops! This field is required")
    private String firstName;


    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    @Min(value = 5,message = "👶 Hey There!" +
            "Children must be at least 5 years old to join in")
    @Max(value = 17,message = "👶 Hey There!" +
            "Children must be 17 years old or younger")
    private Integer age;

    @Column(name = "gender")
    @NotNull(message = "\uD83D\uDEA8 Oops! This field is required")
    private String gender;

    @Column(name = "relation")
    @NotNull(message = "\uD83D\uDEA8 Oops! This field is required")
    private String relation;

    public Child(User theUser){
        this.parent=theUser;
    }

}
