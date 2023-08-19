package com.kuntia.springauthjwtrbac.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/access")
public class AccessController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('VIEWER')")
    public String get() {
        return "GET:: access resource";
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
    public String put() {
        return "UPDATE:: access resource";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
    public String post() {
        return "CREATE:: access resource";
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
    public String delete() {
        return "DELETE:: access resource";
    }

}
