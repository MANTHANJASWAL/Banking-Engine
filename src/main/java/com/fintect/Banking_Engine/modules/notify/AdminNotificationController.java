package com.fintect.Banking_Engine.modules.notify;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/notifications")
public class AdminNotificationController {

    private final AdminNotificationService adminNotificationService;

    public AdminNotificationController(AdminNotificationService adminNotificationService) {
        this.adminNotificationService = adminNotificationService;
    }

    // Secure this endpoint so only admins can trigger emails
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/broadcast")
    public ResponseEntity<String> sendBroadcastEmail(@RequestBody AdminEmailRequestDto request) {
        adminNotificationService.processAdminEmail(request);
        return ResponseEntity.ok("Emails dispatched to background queue successfully.");
    }
}