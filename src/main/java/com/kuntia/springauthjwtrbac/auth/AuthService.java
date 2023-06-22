package com.kuntia.springauthjwtrbac.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kuntia.springauthjwtrbac.auth.dto.AuthResponseDto;
import com.kuntia.springauthjwtrbac.auth.dto.LoginRequestDto;
import com.kuntia.springauthjwtrbac.auth.dto.RegisterRequestDto;
import com.kuntia.springauthjwtrbac.auth.jwt.JwtAccessService;
import com.kuntia.springauthjwtrbac.auth.jwt.JwtRefreshService;
import com.kuntia.springauthjwtrbac.user.User;
import com.kuntia.springauthjwtrbac.user.UserRepository;
import com.kuntia.springauthjwtrbac.util.EncryptionUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${APP_KEY}")
    private String appKey;

    @Value("${APP_ALGORITHM}")
    private String appAlgorithm;

    @Value("${APP_IV}")
    private String appIv;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAccessService jwtAccessService;
    private final JwtRefreshService jwtRefreshService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterRequestDto dto) {
        var emailExists = userRepository.countByEmail(dto.getEmail());
        if (emailExists > 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "this email already exists");
        }

        var decryptedPassword = EncryptionUtils.decrypt(dto.getPassword(), appKey, appAlgorithm, appIv);
        var passwordHash = passwordEncoder.encode(decryptedPassword);
        var user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordHash)
                .role(Role.UNASSIGNED)
                .build();

        userRepository.save(user);

        return this.login(new LoginRequestDto(dto.getEmail(), dto.getPassword()));

        // TODO: should send verification email and return tokens with user data
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        var decryptedPassword = EncryptionUtils.decrypt(dto.getPassword(), appKey, appAlgorithm, appIv);
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), decryptedPassword);

        authenticationManager.authenticate(authenticationToken);

        var user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect credentials"));

        var accessToken = jwtAccessService.signToken(new Auth(user));
        var refreshToken = jwtRefreshService.signToken(new Auth(user));

        user.setRefreshToken(passwordEncoder.encode(refreshToken));
        userRepository.save(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // TODO: should return tokens with user data
    }

    public AuthResponseDto refresh(String refreshToken) {
        final String emailFromJwt = jwtRefreshService.extractUsername(refreshToken);
        if (emailFromJwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid refresh token");
        }

        boolean isRefreshTokenExpired = jwtRefreshService.isTokenExpired(refreshToken);
        if (isRefreshTokenExpired) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh token has expired");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(emailFromJwt);
        boolean isRefreshTokenValid = jwtRefreshService.isTokenValid(refreshToken, userDetails);
        if (!isRefreshTokenValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid refresh token");
        }

        var user = userRepository.findByEmail(emailFromJwt)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid refresh token"));

        var isMatchToken = passwordEncoder.matches(refreshToken, user.getRefreshToken());
        if (!isMatchToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid refresh token");
        }

        var newAccessToken = jwtAccessService.signToken(new Auth(user));
        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .build();
    }

}
