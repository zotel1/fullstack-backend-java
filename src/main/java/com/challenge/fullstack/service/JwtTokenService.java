package com.challenge.fullstack.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {

    private final String apiSecret = "TExBVkVfTVVZX1NFQ1JFVEzE3Zmxu7BSGSJx72BSBXM";

    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 15; // 15 minutos
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24; // 24 horas

    public String generateToken(UserDetails userDetails, String role, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", "ROLE_" + role);

        SecretKey secretKey = Keys.hmacShaKeyFor(apiSecret.getBytes(StandardCharsets.UTF_8)); // Genera la clave

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(secretKey, SignatureAlgorithm.HS256) // Usa la clave y el algoritmo
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails, String role, String username) {
        Map<String, Object> claims = new HashMap<>();
        //claims.put("role", role);
        claims.put("username", username);
        claims.put("role", "ROLE_" + role);

        SecretKey secretKey = Keys.hmacShaKeyFor(apiSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(secretKey ,SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            System.err.println("Error al validar el token: " + e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(apiSecret.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("El token ha expirado", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al procesar el token", e);
        }
    }

    @PostConstruct
    public void validateSecret() {
        if (apiSecret == null || apiSecret.isEmpty()) {
            throw new IllegalStateException("El valor de apiSecret no está configurado");
        }
        System.out.println("JwtSecret configurado correctamente");
    }
}
