package com.fintect.Banking_Engine.modules.auth;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3334)
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash; // Stored using Argon2

    // ADD THIS FIELD: Required for findAllByRole
    @Column(nullable = false)
    private String role = "USER";
}