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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", indexes = {
        @Index(columnList = "username", name = "users_username_key"),
        @Index(columnList = "email", name = "users_email_key")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private String refreshToken;
    private String verificationToken;

    @Column(length = 6)
    private String otp;
    private String firstName;
    private String lastName;
    private String dialCode;
    private String phone;
    private String profilePicture;
    private String address;
    private String city;

    @Column(length = 5)
    private String zipCode;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum", nullable = false)
    private Role role = Role.UNASSIGNED;

    @Column(nullable = false)
    private Boolean isVerified = false;

    @Column(nullable = false)
    private Boolean isTncAccepted = false;
    private Date tncAcceptedDate;
    private Date verifiedDate;

    @Override
    public String toString() {
        return "User [username=" + username + ", email=" + email + ", password=" + password + ", refreshToken="
                + refreshToken + ", verificationToken=" + verificationToken + ", otp=" + otp + ", firstName="
                + firstName + ", lastName=" + lastName + ", dialCode=" + dialCode + ", phone=" + phone
                + ", profilePicture=" + profilePicture + ", address=" + address + ", city=" + city + ", zipCode="
                + zipCode + ", role=" + role + ", isVerified=" + isVerified + ", isTncAccepted=" + isTncAccepted
                + ", tncAcceptedDate=" + tncAcceptedDate + ", verifiedDate=" + verifiedDate + "]";
    }

}