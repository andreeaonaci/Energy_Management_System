package service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    // Generate JWT token
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Use HS512 for signing
                .compact();
    }

    // Get username from JWT token
    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validate JWT token
    public boolean validateToken(String authToken) {
        try {
            // Using Jwts.parser() instead of parserBuilder()
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(jwtSecret)  // Set the signing key
                    .parseClaimsJws(authToken); // Parse the token

            return !claims.getBody().getExpiration().before(new Date()); // Check expiration
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (JwtException ex) {
            System.out.println("JWT token is invalid");
        }
        return false; // Token is invalid
    }
}
