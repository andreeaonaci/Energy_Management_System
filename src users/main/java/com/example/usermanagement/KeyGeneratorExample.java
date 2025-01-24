package com.example.usermanagement;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyGeneratorExample {
    public static void main(String[] args) throws Exception {
        // Initialize the KeyGenerator for the HMAC-SHA-256 algorithm
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        keyGenerator.init(256); // Key size in bits (256 bits = 32 bytes)

        // Generate the secret key
        SecretKey secretKey = keyGenerator.generateKey();

        // Print the secret key in Base64 encoding
        System.out.println("Secret Key (Base64): " + java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    }
}
