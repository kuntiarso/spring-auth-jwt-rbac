package com.kuntia.springauthjwtrbac.user;

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

@Entity
@Table(name = "roles", indexes = {
        @Index(columnList = "name", name = "roles_name_key", unique = true)
})
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum", nullable = false)
    @Builder.Default
    private ERole name = ERole.ROLE_VIEWER;

}
