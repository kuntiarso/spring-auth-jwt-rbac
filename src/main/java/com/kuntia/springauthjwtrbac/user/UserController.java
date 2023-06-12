package com.kuntia.springauthjwtrbac.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<User> getUser(@RequestParam String email) {
        User user = userService.getUser(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
