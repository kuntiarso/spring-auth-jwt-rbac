package com.kuntia.springauthjwtrbac.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuntia.springauthjwtrbac.user.dto.EncryptRequest;
import com.kuntia.springauthjwtrbac.util.EncryptionUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.username}")
    private String USERNAME;

    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @Value("${JWT_REFRESH_SECRET}")
    private String jwtRefreshSecret;

    @Value("${APP_KEY}")
    private String appKey;

    @Value("${APP_ALGORITHM}")
    private String appAlgorithm;

    @Value("${APP_IV}")
    private String appIv;

    @Autowired
    private Environment config;

    @PostMapping("/encrypt")
    public ResponseEntity<?> encryptInput(@Valid @RequestBody EncryptRequest body) throws Exception {
        var encryptedInput = EncryptionUtils.encrypt(body.getPassword(), appKey, appAlgorithm, appIv);

        Map<String, String> response = new HashMap<>();
        response.put("result", encryptedInput);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/url")
    public ResponseEntity<?> getUrl() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("url", URL);
        env.put("username", USERNAME);
        env.put("password", PASSWORD);
        env.put("access", config.getProperty("JWT_ACCESS_SECRET"));
        env.put("refreh", jwtRefreshSecret);

        return new ResponseEntity<>(env, HttpStatus.OK);
    }
}
