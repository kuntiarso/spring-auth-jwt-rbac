package com.kuntia.springauthjwtrbac.auth;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.kuntia.springauthjwtrbac.auth.dto.AuthResponse;
import com.kuntia.springauthjwtrbac.auth.dto.LoginRequest;
import com.kuntia.springauthjwtrbac.auth.dto.RegisterRequest;
import com.kuntia.springauthjwtrbac.auth.jwt.JwtAccessService;
import com.kuntia.springauthjwtrbac.auth.jwt.JwtRefreshService;
import com.kuntia.springauthjwtrbac.user.UserAuthority;
import com.kuntia.springauthjwtrbac.user.ERole;
import com.kuntia.springauthjwtrbac.user.Role;
import com.kuntia.springauthjwtrbac.user.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAccessService jwtAccessService;
    private final JwtRefreshService jwtRefreshService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest body) {
        if (userRepository.existsByUsername(body.getUsername())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error:: this username already exists");
        }
        if (userRepository.existsByEmail(body.getEmail())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error:: this email already exists");
        }

        var decryptedPassword = EncryptionUtils.decrypt(body.getPassword(), appKey, appAlgorithm, appIv);
        var passwordHash = passwordEncoder.encode(decryptedPassword);
        var user = User.builder()
                .firstName(body.getFirstName())
                .lastName(body.getLastName())
                .username(body.getUsername())
                .email(body.getEmail())
                .password(passwordHash)
                .build();

        Set<String> inputRoles = body.getRoles();
        Set<Role> roles = new HashSet<>();

        if (inputRoles == null) {
            Role viewerRole = roleRepository.findByName(ERole.ROLE_VIEWER)
                    .orElseThrow(() -> new RuntimeException("Error:: role is not found"));
            roles.add(viewerRole);
        } else {
            inputRoles.forEach(role -> {
                switch (role) {
                    case "ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error:: role is not found"));
                        roles.add(adminRole);
                        break;
                    case "EDITOR":
                        Role editorRole = roleRepository.findByName(ERole.ROLE_EDITOR)
                                .orElseThrow(() -> new RuntimeException("Error:: role is not found"));
                        roles.add(editorRole);
                        break;
                    default:
                        Role viewerRole = roleRepository.findByName(ERole.ROLE_VIEWER)
                                .orElseThrow(() -> new RuntimeException("Error:: role is not found"));
                        roles.add(viewerRole);
                        break;
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return this.login(new LoginRequest(body.getEmail(), body.getPassword()));
    }

    public AuthResponse login(LoginRequest body) {
        var decryptedPassword = EncryptionUtils.decrypt(body.getPassword(), appKey, appAlgorithm, appIv);
        var authenticationToken = new UsernamePasswordAuthenticationToken(body.getEmail(), decryptedPassword);
        var authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = userRepository.findByEmail(body.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "incorrect credentials"));

        var accessToken = jwtAccessService.signToken(new UserAuthority(user));
        var refreshToken = jwtRefreshService.signToken(new UserAuthority(user));

        user.setRefreshToken(passwordEncoder.encode(refreshToken));
        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse refresh(String refreshToken) {
        if (jwtRefreshService.validateToken(refreshToken)) {
            // NOTE: extractUsername here means get email value from token
            var email = jwtRefreshService.extractUsername(refreshToken);
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                            "Error:: invalid refresh token"));

            var isMatchToken = passwordEncoder.matches(refreshToken, user.getRefreshToken());
            if (!isMatchToken) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error:: invalid refresh token");
            }

            var newAccessToken = jwtAccessService.signToken(new UserAuthority(user));
            return AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error:: invalid refresh token");
        }
    }

}
