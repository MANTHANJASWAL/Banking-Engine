package com.fintect.Banking_Engine.modules.aml;

import com.fintect.Banking_Engine.core.dsa.GraphCentralityEngine;
import com.fintect.Banking_Engine.core.dsa.TarjansCycleDetectionEngine;
import com.fintect.Banking_Engine.modules.ledger.Account;
import com.fintect.Banking_Engine.modules.ledger.AccountRepository;
import com.fintect.Banking_Engine.modules.ledger.Transaction;
import com.fintect.Banking_Engine.modules.ledger.TransactionRepository;
import com.fintect.Banking_Engine.modules.ledger.LedgerEntry;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Manthan Jaswal (Roll: 3334)
 */
@Service
public class AmlService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AmlService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public CycleResponseDto detectSmurfingCycles() {
        Map<UUID, List<UUID>> transactionGraph = buildTransactionGraph();
        TarjansCycleDetectionEngine engine = new TarjansCycleDetectionEngine();
        List<List<UUID>> cycles = engine.findCycles(transactionGraph);
        return new CycleResponseDto(cycles.size(), cycles);
    }

    @Transactional(readOnly = true)
    public RiskScoreResponseDto calculateRisk(UUID accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new EntityNotFoundException("Account not found");
        }

        Map<UUID, List<UUID>> outEdges = buildTransactionGraph();
        Map<UUID, List<UUID>> inEdges = buildInboundTransactionGraph();

        double centrality = GraphCentralityEngine.calculateDegreeCentrality(accountId, outEdges, inEdges);
        String riskLevel = centrality > 0.5 ? "HIGH" : (centrality > 0.2 ? "MEDIUM" : "LOW");

        return new RiskScoreResponseDto(accountId, centrality, riskLevel);
    }

    @Transactional
    public void freezeAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        account.setFrozen(true);
        accountRepository.save(account);
    }

    // Helper: Builds a Directed Graph (From Account ID -> To Account IDs)
    private Map<UUID, List<UUID>> buildTransactionGraph() {
        Map<UUID, List<UUID>> graph = new HashMap<>();
        List<Transaction> transactions = transactionRepository.findAll();

        for (Transaction tx : transactions) {
            UUID from = null;
            UUID to = null;
            for (LedgerEntry entry : tx.getEntries()) {
                if (entry.getAmount().compareTo(BigDecimal.ZERO) < 0) from = entry.getAccount().getId();
                if (entry.getAmount().compareTo(BigDecimal.ZERO) > 0) to = entry.getAccount().getId();
            }
            if (from != null && to != null) {
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                graph.putIfAbsent(to, new ArrayList<>()); // Ensure destination nodes exist in graph keys
            }
        }
        return graph;
    }

    // Helper: Builds Inbound Edges for Centrality
    private Map<UUID, List<UUID>> buildInboundTransactionGraph() {
        Map<UUID, List<UUID>> inGraph = new HashMap<>();
        Map<UUID, List<UUID>> outGraph = buildTransactionGraph();

        for (UUID node : outGraph.keySet()) {
            inGraph.putIfAbsent(node, new ArrayList<>());
        }

        for (Map.Entry<UUID, List<UUID>> entry : outGraph.entrySet()) {
            UUID from = entry.getKey();
            for (UUID to : entry.getValue()) {
                inGraph.get(to).add(from);
            }
        }
        return inGraph;
    }
}