package com.luv2code.emailVerification.demo.controller;

import com.luv2code.emailVerification.demo.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/start")
public class Sitecontroller {
    @GetMapping("/")
    public String homePage(){
        return "Get-Started";
    }




}
