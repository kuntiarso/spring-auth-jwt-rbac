package com.kuntia.springauthjwtrbac.user;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

}
