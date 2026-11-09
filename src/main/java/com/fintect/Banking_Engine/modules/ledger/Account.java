package com.fintect.Banking_Engine.modules.ledger;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // SECURITY: Used strictly for JWT authorization and ownership isolation
    @Column(name = "owner_username", nullable = false, unique = true)
    private String ownerUsername;

    // DISPLAY: Used for frontend UI rendering
    @Column(name = "account_holder_name", nullable = false, length = 100)
    private String accountHolderName;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Builder.Default
    @Column(nullable = false)
    private boolean isFrozen = false;

    @Version
    private Long version;
}