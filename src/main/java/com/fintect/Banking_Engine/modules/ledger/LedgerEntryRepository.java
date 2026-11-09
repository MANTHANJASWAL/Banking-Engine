package com.fintect.Banking_Engine.modules.ledger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {

    @Query("SELECT e FROM LedgerEntry e " +
            "WHERE e.account.id = :accountId " +
            "AND (:direction IS NULL OR e.direction = :direction) " +
            "AND (cast(:startDate as timestamp) IS NULL OR e.transaction.timestamp >= :startDate) " +
            "AND (cast(:endDate as timestamp) IS NULL OR e.transaction.timestamp <= :endDate) " +
            "ORDER BY e.transaction.timestamp DESC")
    Page<LedgerEntry> findFilteredStatements(
            @Param("accountId") UUID accountId,
            @Param("direction") LedgerEntry.EntryDirection direction,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            Pageable pageable
    );
}