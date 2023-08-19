package com.kuntia.springauthjwtrbac.util;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public abstract class JwtUtils {

    public static Key getSigningKey(String secret) {
        byte[] secretBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(secretBytes);
    }

    private static Claims extractJwt(String token, String secret) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver,
            String secret) {
        final Claims claims = extractJwt(token, secret);
        return claimsResolver.apply(claims);
    }

    public static String generateToken(
            Map<String, Object> claims,
            UserDetails userDetails,
            String secret,
            long expiresIn) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiresIn))
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

}
