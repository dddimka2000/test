package com.CMS.kinoCMS.admin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class login {
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
