package com.fintect.Banking_Engine.modules.notify;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@RestController
@RequestMapping("/api/v1/notify")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Void> broadcast(@RequestBody String message) {
        AlertDto alert = new AlertDto(UUID.randomUUID().toString(), "HIGH", message, Instant.now().toEpochMilli());
        notificationService.broadcastAlert(alert);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/buffer")
    public ResponseEntity<List<AlertDto>> pollBuffer() {
        return ResponseEntity.ok(notificationService.pollRecentAlerts());
    }
}