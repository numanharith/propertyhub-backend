package com.propertyhub.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.expiration.ms}")
    private long jwtExpirationInMs;

    private Key secretKey;

    @PostConstruct
    public void init() {
        // Ensure the secret key is strong enough for HS256
        // A common practice is to use a Base64 encoded string for the secret.
        // For HS256, the key size should be at least 256 bits (32 bytes).
        // If your secretString is shorter, this might lead to weak keys.
        // Consider generating a secure key and storing it properly.
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) { // Changed from UserDetails for flexibility if needed
        Map<String, Object> claims = new HashMap<>();
        // Add any custom claims here if needed
        // For example, roles: userDetails.getAuthorities()...
        return createToken(claims, username);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Add roles to claims
        claims.put("roles", userDetails.getAuthorities().stream().map(Object::toString).toList());
        return createToken(claims, userDetails.getUsername()); // Username from UserDetails (which is email in our case)
    }


    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

     public Boolean validateToken(String token) { // General validation without UserDetails
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) { // Catches various JWT exceptions (ExpiredJwtException, MalformedJwtException, etc.)
            return false;
        }
    }
}
