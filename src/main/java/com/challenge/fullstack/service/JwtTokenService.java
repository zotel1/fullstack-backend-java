package com.challenge.fullstack.service;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {

    //private final String jwtSecret = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsInN1YiI6ImZvcmJ0ZWNoIiwiaWF0IjoxNzM2NzQyNzkwLCJleHAiOjE3MzY3NDM2OTB9.SaKuzFpIyid1XFmKI5zmvxRI0nOX8736k4oGiS0SsOI";

    //@Value("${api.security.secret}")
    private final String apiSecret = "TExBVkVfTVVZX1NFQ1JFVEzE3Zmxu7BSGSJx72BSBXM";

    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 15; // 15 minutos de validez
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24; // 24 horas para el token de refresco

    //public String generateToken(UserDetails userDetails, String role) {
      //  Map<String, Object> claims = new HashMap<>();
        //claims.put("role", role);

        //String token = Jwts.builder()
          //      .setClaims(claims)
            //    .setSubject(userDetails.getUsername())
              //  .setIssuedAt(new Date())
                //.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
      //          .signWith(SignatureAlgorithm.HS256, apiSecret.getBytes(StandardCharsets.UTF_8))
        //        .compact();

      //  System.out.println("Token de acceso generado: " + token);
        //return token;
    //}

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().toString());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, apiSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }



    public String generateRefreshToken(UserDetails userDetails, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, apiSecret.getBytes(StandardCharsets.UTF_8))
                .compact();

        System.out.println("Token de refresco generado: " + refreshToken);
        return refreshToken;
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
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("Formato de token no soportado", e);
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("Token malformado", e);
        } catch (SignatureException e) {
            throw new IllegalArgumentException("Firma inválida del token", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al procesar el token", e);
        }
    }

    @PostConstruct
    public void validateSecret() {
        if (apiSecret == null || apiSecret.isEmpty()) {
            throw new IllegalStateException("El valor de jwtSecret no está configurado");
        }
        System.out.println("JwtSecret configurado: " + apiSecret);
    }
}
