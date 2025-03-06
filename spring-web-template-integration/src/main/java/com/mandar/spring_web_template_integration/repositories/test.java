package com.mandar.spring_web_template_integration.repositories;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test {
    public static void main(String[] args) {
        //debugging the password change issue
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hashedPassword = "$2a$10$Y6efC68YFSI.LBTeipcFHO8WXzaItXKT7FOqx/4XXGt6kcuKYdz.6"; // Your hash
        String inputPassword = "pass787"; // Replace with the password you want to check

        if (encoder.matches(inputPassword, hashedPassword)) {
            System.out.println("Password is correct!");
        } else {
            System.out.println("Incorrect password.");
        }


    }
}
