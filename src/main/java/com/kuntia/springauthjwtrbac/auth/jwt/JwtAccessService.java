package com.kuntia.springauthjwtrbac.auth.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtAccessService extends JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtAccessService.class);

    public JwtAccessService(
            @Value("${JWT_ACCESS_SECRET}") String jwtAccessSecret,
            @Value("${JWT_ACCESS_EXPIRES_IN}") long jwtAccessExpiresIn) {
        super("Access token", jwtAccessSecret, jwtAccessExpiresIn, log);
    }

}
