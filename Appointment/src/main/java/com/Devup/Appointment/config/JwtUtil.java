package com.Devup.Appointment.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {


    private String secret="ShivAvashiaDevUpSpringBootSecureKey123";

    public Claims extractClaims(String token) throws Exception {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) throws Exception {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) throws Exception {
        return (String) extractClaims(token).get("role");
    }
}

