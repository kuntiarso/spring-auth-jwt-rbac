package com.kuntia.springauthjwtrbac.auth.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kuntia.springauthjwtrbac.util.JwtUtils;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtAccessService implements JwtService {

    @Value("${JWT_ACCESS_SECRET}")
    private String jwtAccessSecret;

    @Value("${JWT_ACCESS_EXPIRES_IN}")
    private long jwtAccessExpiresIn;

    @Override
    public Date extractExpiration(String token) {
        return JwtUtils.extractClaim(token, Claims::getExpiration, jwtAccessSecret);
    }

    @Override
    public String extractUsername(String token) {
        return JwtUtils.extractClaim(token, Claims::getSubject, jwtAccessSecret);
    }

    @Override
    public String signToken(UserDetails userDetails) {
        return signToken(new HashMap<>(), userDetails);
    }

    @Override
    public String signToken(Map<String, Object> claims, UserDetails userDetails) {
        return JwtUtils.generateToken(
                claims,
                userDetails,
                jwtAccessSecret,
                jwtAccessExpiresIn);
    }

    @Override
    public boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.before(new Date());
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        final String expectedEmail = userDetails.getUsername();
        return (email.equals(expectedEmail)) && !isTokenExpired(token);
    }

}
