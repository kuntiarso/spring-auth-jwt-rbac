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

import com.kuntia.springauthjwtrbac.auth.dto.AuthResponseDto;
import com.kuntia.springauthjwtrbac.auth.dto.LoginRequestDto;
import com.kuntia.springauthjwtrbac.auth.dto.RegisterRequestDto;

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

    @Value("${COOKIE_VERIFICATION_MAX_AGE}")
    private long cookieVerificationMaxAge;

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto dto) {
        var tokens = authService.register(dto);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", tokens.getRefreshToken())
                .secure(cookieSecure)
                .httpOnly(cookieHttpOnly)
                .sameSite(cookieSameSite)
                .maxAge(cookieRefreshMaxAge)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<AuthResponseDto>(tokens, headers, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto dto) {
        var tokens = authService.login(dto);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", tokens.getRefreshToken())
                .secure(cookieSecure)
                .httpOnly(cookieHttpOnly)
                .sameSite(cookieSameSite)
                .maxAge(cookieRefreshMaxAge)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<AuthResponseDto>(tokens, headers, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@CookieValue("refreshToken") String refreshToken) {
        var tokens = authService.refresh(refreshToken);
        return new ResponseEntity<AuthResponseDto>(tokens, HttpStatus.OK);
    }

}
