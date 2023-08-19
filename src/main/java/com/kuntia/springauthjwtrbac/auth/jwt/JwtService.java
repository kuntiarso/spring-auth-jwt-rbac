package com.kuntia.springauthjwtrbac.auth.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;

import com.kuntia.springauthjwtrbac.util.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtService {

    private String tokenName;
    private String jwtSecret;
    private long expiresIn;
    private Logger log;

    public JwtService(String tokenName, String jwtSecret, long expiresIn, Logger log) {
        this.tokenName = tokenName;
        this.jwtSecret = jwtSecret;
        this.expiresIn = expiresIn;
        this.log = log;
    }

    public Date extractExpiration(String token) {
        return JwtUtils.extractClaim(token, Claims::getExpiration, jwtSecret);
    }

    public String extractUsername(String token) {
        return JwtUtils.extractClaim(token, Claims::getSubject, jwtSecret);
    }

    public String signToken(UserDetails userDetails) {
        return signToken(new HashMap<>(), userDetails);
    }

    public String signToken(Map<String, Object> claims, UserDetails userDetails) {
        return JwtUtils.generateToken(
                claims,
                userDetails,
                jwtSecret,
                expiresIn);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(JwtUtils.getSigningKey(jwtSecret)).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid " + tokenName.toLowerCase() + ": {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error(tokenName + " is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error(tokenName + " is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(tokenName + " argument is empty: {}", e.getMessage());
        }

        return false;
    }

}
