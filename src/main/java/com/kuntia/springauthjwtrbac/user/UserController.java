package com.kuntia.springauthjwtrbac.user;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getUser(Principal principal) {
        System.out.println(principal.getName());
        User user = userService.getUser(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
