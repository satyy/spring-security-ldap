package com.satyy.ldap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Satyam Singh (satyamsingh00@gmail.com)
 */
@RestController
public class HomeController {

    @GetMapping("/home")
    public String index() {
        return "Welcome to the home page!";
    }
}
