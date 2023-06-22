package com.kuntia.springauthjwtrbac.user;

import java.util.Date;

import com.kuntia.springauthjwtrbac.auth.Role;
import com.kuntia.springauthjwtrbac.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users", indexes = {
        @Index(columnList = "username", name = "users_username_key", unique = true),
        @Index(columnList = "email", name = "users_email_key", unique = true)
})
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    private String username;

    @Column(nullable = false)
    private String email;

    @Column(columnDefinition = "text", nullable = false)
    @ToString.Exclude
    private String password;
    private String refreshToken;
    private String verificationToken;

    @Column(length = 6)
    private String otp;
    private String firstName;
    private String lastName;
    private String phone;
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum", nullable = false)
    @Builder.Default
    private Role role = Role.UNASSIGNED;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isVerified = false;
    private Date verifiedDate;

}