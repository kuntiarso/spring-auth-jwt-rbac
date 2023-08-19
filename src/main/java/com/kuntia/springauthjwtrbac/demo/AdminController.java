package com.kuntia.springauthjwtrbac.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String get() {
        return "GET:: admin resource";
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String put() {
        return "UPDATE:: admin resource";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String post() {
        return "CREATE:: admin resource";
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String delete() {
        return "DELETE:: admin resource";
    }

}
