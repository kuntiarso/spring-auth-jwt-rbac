package com.kuntia.springauthjwtrbac.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.kuntia.springauthjwtrbac.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    private String firstName;
    private String lastName;
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(columnDefinition = "text", nullable = false)
    @ToString.Exclude
    private String password;
    private String refreshToken;
    private String verificationToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Column(length = 6)
    private String otp;
    private Date verifiedDate;

}