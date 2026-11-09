package com.fintect.Banking_Engine.modules.notify;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    // Your existing asynchronous ledger listener
    @Async
    @EventListener
    public void handleTransactionCompleted(TransactionCompletedEvent event) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== ASYNC NOTIFICATION DISPATCHED ===");
        System.out.println("Account: " + event.accountId());
        System.out.println("Action:  " + event.action());
        System.out.println("Amount:  " + event.amount());
        System.out.println("Txn Ref: " + event.transactionId());
        System.out.println("=====================================\n");
    }

    // NEW: Resolves the broadcastAlert compilation error
    public void broadcastAlert(AlertDto alertDto) {
        // Placeholder for future manual alert broadcasting logic
        System.out.println("Broadcasting alert: " + alertDto);
    }

    // NEW: Resolves the pollRecentAlerts compilation error
    public List<AlertDto> pollRecentAlerts() {
        // Placeholder for future alert retrieval logic
        return new ArrayList<>();
    }
}