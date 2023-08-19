package com.kuntia.springauthjwtrbac.auth.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtRefreshService extends JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtRefreshService.class);

    public JwtRefreshService(
            @Value("${JWT_REFRESH_SECRET}") String jwtRefreshSecret,
            @Value("${JWT_REFRESH_EXPIRES_IN}") long jwtRefreshExpiresIn) {
        super("Refresh token", jwtRefreshSecret, jwtRefreshExpiresIn, log);
    }

}
