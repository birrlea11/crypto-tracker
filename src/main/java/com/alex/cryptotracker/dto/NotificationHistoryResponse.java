package com.alex.cryptotracker.dto;

import com.alex.cryptotracker.model.AlertDirection;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationHistoryResponse {
    private Long id;
    private String userEmail;
    private String coinId;
    private Double priceAtNotification;
    private AlertDirection direction;
    private LocalDateTime sentAt;
    private String status;
}