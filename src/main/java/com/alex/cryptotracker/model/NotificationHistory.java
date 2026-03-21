package com.alex.cryptotracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class NotificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private String coinId;
    private Double priceAtNotification;

    @Enumerated(EnumType.STRING)
    private AlertDirection direction;

    private LocalDateTime sentAt;
    private String status; // Ex: "SUCCESS" sau "FAILED"
}