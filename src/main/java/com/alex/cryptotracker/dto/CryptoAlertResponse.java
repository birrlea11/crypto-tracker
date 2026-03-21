package com.alex.cryptotracker.dto;

import com.alex.cryptotracker.model.AlertDirection;
import lombok.Data;

@Data
public class CryptoAlertResponse {
    private Long id;
    private String coinId;
    private Double targetPrice;
    private AlertDirection direction;
    private Boolean isActive;
}