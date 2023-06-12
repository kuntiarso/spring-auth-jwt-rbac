package com.kuntia.springauthjwtrbac.user;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public User getUser(String email) {
        return userRepository.findFirstByEmail(email).orElseThrow();
    }

}
