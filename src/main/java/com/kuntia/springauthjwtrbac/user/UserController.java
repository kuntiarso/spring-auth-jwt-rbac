package com.kuntia.springauthjwtrbac.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.username}")
    private String USERNAME;

    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @GetMapping("/url")
    public ResponseEntity<?> getUrl() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("url", URL);
        env.put("username", USERNAME);
        env.put("password", PASSWORD);

        return new ResponseEntity<>(env, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<User> getUser(@RequestParam String email) {
        User user = userService.getUser(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
