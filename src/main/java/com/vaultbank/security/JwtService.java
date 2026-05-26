package com.vaultbank.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms:3600000}")
    private long expirationMs;

    @Value("${jwt.refresh-expiration-ms:86400000}")
    private long refreshExpirationMs;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(String email, String rol) {
        return Jwts.builder()
                .setSubject(email)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generarRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("tipo", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean esTokenAccesoValido(String token) {
        Claims claims = obtenerClaims(token);
        return claims != null && claims.get("tipo") == null;
    }

    public boolean esRefreshTokenValido(String token) {
        Claims claims = obtenerClaims(token);
        return claims != null && "refresh".equals(claims.get("tipo"));
    }

    public String obtenerEmail(String token) {
        Claims claims = obtenerClaims(token);
        return claims == null ? null : claims.getSubject();
    }

    public String obtenerRol(String token) {
        Claims claims = obtenerClaims(token);
        return claims == null ? null : (String) claims.get("rol");
    }

    private Claims obtenerClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
