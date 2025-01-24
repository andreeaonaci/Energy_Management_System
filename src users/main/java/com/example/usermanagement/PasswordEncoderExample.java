package com.example.usermanagement;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderExample {

    public static void main(String[] args) {
        // Create a BCryptPasswordEncoder instance
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Encode the password
        String rawPassword = "petrut1234";  // Your raw password
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Print the encoded password
        System.out.println("Encoded password: " + encodedPassword);
    }
}
