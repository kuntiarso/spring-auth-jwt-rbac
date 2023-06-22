package com.kuntia.springauthjwtrbac.auth.jwt;

import java.util.Date;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    public Date extractExpiration(String token);

    public String extractUsername(String token);

    public String signToken(UserDetails userDetails);

    public String signToken(Map<String, Object> claims, UserDetails userDetails);

    public boolean isTokenExpired(String token);

    public boolean isTokenValid(String token, UserDetails userDetails);

}
