package com.kuntia.springauthjwtrbac.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthorityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    // NOTE: username argument means email here
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .map(UserAuthority::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username/email not found, user not authorized"));
    }

}
