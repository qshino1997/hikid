package com.example.controller.Auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserLoginController {

    @GetMapping("/login")
    public String login(){
        System.out.println("hy login");
        return "home";
    }
}
