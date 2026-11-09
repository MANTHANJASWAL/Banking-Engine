package com.fintect.Banking_Engine.modules.ledger;

import com.fintect.Banking_Engine.core.dsa.MerkleTreeEngine;
import com.fintect.Banking_Engine.modules.notify.TransactionCompletedEvent;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@Service
public class LedgerService {
    private final ApplicationEventPublisher eventPublisher;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    public LedgerService(AccountRepository accountRepository,
                         ApplicationEventPublisher eventPublisher,
                         TransactionRepository transactionRepository,
                         LedgerEntryRepository ledgerEntryRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Account createAccount(AccountRequestDto request) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (accountRepository.findByOwnerUsername(currentUsername).isPresent()) {
            throw new IllegalStateException("An account already exists for this user.");
        }

        Account account = Account.builder()
                .ownerUsername(currentUsername)
                .accountHolderName(request.accountHolderName())
                .currency(request.currency())
                .balance(request.initialBalance() != null ? request.initialBalance() : BigDecimal.ZERO)
                .build();

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account getAccount() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.findByOwnerUsername(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("No account found for user: " + currentUsername));
    }

    public Account getAccountById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Transaction executeTransfer(TransferRequestDto request) {
        if (transactionRepository.existsByIdempotencyKey(request.idempotencyKey())) {
            throw new IllegalStateException("Transaction already processed");
        }

        Account fromAccount = getAccountById(request.fromAccountId());
        Account toAccount = getAccountById(request.toAccountId());

        if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount()));
        toAccount.setBalance(toAccount.getBalance().add(request.amount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = Transaction.builder()
                .idempotencyKey(request.idempotencyKey())
                .timestamp(Instant.now())
                .build();

        LedgerEntry debit = createEntry(transaction, fromAccount, request.amount().negate());
        LedgerEntry credit = createEntry(transaction, toAccount, request.amount());

        BigDecimal zeroSumCheck = debit.getAmount().add(credit.getAmount());
        if (zeroSumCheck.compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Ledger constraint violated: Zero-Sum check failed.");
        }

        transaction.setEntries(List.of(debit, credit));

        // FIXED: Save first, then publish transfer event
        Transaction savedTransaction = transactionRepository.save(transaction);

        eventPublisher.publishEvent(new TransactionCompletedEvent(
                savedTransaction.getId(),
                fromAccount.getId(),
                "TRANSFER",
                request.amount()
        ));

        return savedTransaction;
    }

    private LedgerEntry createEntry(Transaction tx, Account acc, BigDecimal amount) {
        String rawData = tx.getIdempotencyKey() + acc.getId().toString() + amount.toPlainString();
        String hash = MerkleTreeEngine.generateHash(rawData);
        LedgerEntry.EntryDirection determinedDirection = amount.compareTo(BigDecimal.ZERO) < 0
                ? LedgerEntry.EntryDirection.DEBIT
                : LedgerEntry.EntryDirection.CREDIT;
        return LedgerEntry.builder()
                .transaction(tx)
                .account(acc)
                .amount(amount)
                .direction(determinedDirection)
                .hash(hash)
                .build();
    }

    @Transactional
    public Transaction processDeposit(UUID accountId, BigDecimal amount, String idempotencyKey) {
        if (transactionRepository.existsByIdempotencyKey(idempotencyKey)) {
            throw new IllegalStateException("Transaction with this idempotency key already exists.");
        }

        Account account = getAccountById(accountId);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .idempotencyKey(idempotencyKey)
                .timestamp(Instant.now())
                .build();

        LedgerEntry depositEntry = createEntry(transaction, account, amount);

        transaction.setEntries(List.of(depositEntry));

        // FIXED: Save first, then publish deposit event
        Transaction savedTransaction = transactionRepository.save(transaction);

        eventPublisher.publishEvent(new TransactionCompletedEvent(
                savedTransaction.getId(),
                accountId,
                "DEPOSIT",
                amount
        ));

        return savedTransaction;
    }

    @Transactional
    public Transaction processWithdrawal(UUID accountId, BigDecimal amount, String idempotencyKey) {
        if (transactionRepository.existsByIdempotencyKey(idempotencyKey)) {
            throw new IllegalStateException("Transaction with this idempotency key already exists.");
        }

        Account account = getAccountById(accountId);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal.");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .idempotencyKey(idempotencyKey)
                .timestamp(Instant.now())
                .build();

        LedgerEntry withdrawalEntry = createEntry(transaction, account, amount.negate());

        transaction.setEntries(List.of(withdrawalEntry));

        // FIXED: Save first, then publish withdrawal event
        Transaction savedTransaction = transactionRepository.save(transaction);

        eventPublisher.publishEvent(new TransactionCompletedEvent(
                savedTransaction.getId(),
                accountId,
                "WITHDRAWAL",
                amount
        ));

        return savedTransaction;
    }

    public Transaction getTransaction(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
    }

    @Transactional(readOnly = true)
    public IntegrityResponseDto auditIntegrity() {
        List<LedgerEntry> entries = ledgerEntryRepository.findAll();
        List<String> hashes = entries.stream().map(LedgerEntry::getHash).collect(Collectors.toList());

        String root = MerkleTreeEngine.calculateMerkleRoot(hashes);

        return new IntegrityResponseDto(
                null,
                root,
                root != null,
                entries.size(),
                Instant.now()
        );
    }
}