package com.fintect.Banking_Engine.modules.ledger;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 * Date: 30/1/26
 */
public interface AccountRepository extends JpaRepository<Account, UUID> {

    // NEW METHOD: Finds the specific account for the authenticated user
    Optional<Account> findByOwnerUsername(String ownerUsername);
}