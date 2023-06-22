package com.kuntia.springauthjwtrbac.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    long countByEmail(String email);

    Optional<User> findByEmail(String email);

}
