package com.example.revatureproject.auth;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long TOKEN_EXPIRATION = 86400000; // 24 hours

    @Autowired
    private HttpServletRequest request;

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractToken() {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) { // Assuming the token cookie name is "token"
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String extractUsernameFromToken() {
        String token = extractToken();
        if (token != null) {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        return null;
    }

    public boolean validateToken() {
        String token = extractToken();
        if (token != null) {
            try {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
