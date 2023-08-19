package com.kuntia.springauthjwtrbac.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuntia.springauthjwtrbac.auth.dto.AuthResponse;
import com.kuntia.springauthjwtrbac.auth.dto.LoginRequest;
import com.kuntia.springauthjwtrbac.auth.dto.RegisterRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${COOKIE_SECURE}")
    private boolean cookieSecure;

    @Value("${COOKIE_HTTP_ONLY}")
    private boolean cookieHttpOnly;

    @Value("${COOKIE_SAME_SITE}")
    private String cookieSameSite;

    @Value("${COOKIE_REFRESH_MAX_AGE}")
    private long cookieRefreshMaxAge;

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest body) {
        var result = authService.register(body);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", result.getRefreshToken())
                .secure(cookieSecure)
                .httpOnly(cookieHttpOnly)
                .sameSite(cookieSameSite)
                .maxAge(cookieRefreshMaxAge)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<AuthResponse>(result, headers, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest body) {
        var result = authService.login(body);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", result.getRefreshToken())
                .secure(cookieSecure)
                .httpOnly(cookieHttpOnly)
                .sameSite(cookieSameSite)
                .maxAge(cookieRefreshMaxAge)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<AuthResponse>(result, headers, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue("refreshToken") String refreshToken) {
        var tokens = authService.refresh(refreshToken);
        return new ResponseEntity<AuthResponse>(tokens, HttpStatus.OK);
    }

}
