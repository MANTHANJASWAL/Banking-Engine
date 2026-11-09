package com.fintect.Banking_Engine.modules.notify;

import com.fintect.Banking_Engine.modules.auth.UserEntity;
import com.fintect.Banking_Engine.modules.auth.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminNotificationService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    public AdminNotificationService(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    @Async // Run on background thread
    @Transactional(readOnly = true)
    public void processAdminEmail(AdminEmailRequestDto request) {
        List<String> targetEmails;

        // Determine who gets the email based on the admin's selection
        switch (request.targetAudience().toUpperCase()) {
            case "EMPLOYEES":
                // Assuming your UserEntity has a Role enum (USER, EMPLOYEE, ADMIN)
                targetEmails = userRepository.findAllByRole("EMPLOYEE")
                        .stream().map(UserEntity::getEmail).toList();
                break;
            case "ALL_USERS":
                targetEmails = userRepository.findAllByRole("USER")
                        .stream().map(UserEntity::getEmail).toList();
                break;
            case "SPECIFIC":
                targetEmails = List.of(request.specificEmail());
                break;
            default:
                throw new IllegalArgumentException("Invalid target audience");
        }

        // Loop through the list and dispatch
        for (String email : targetEmails) {
            sendEmail(email, request.subject(), request.messageBody());
        }

        System.out.println("Admin broadcast completed for " + targetEmails.size() + " recipients.");
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("admin@fintect.com"); // The bank's official sender address
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}