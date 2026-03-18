package com.alex.cryptotracker.dto;

import lombok.Data;

@Data
public class CryptoAlertResponse {
    private Long id;
    private String coinId;
    private Double targetPrice;
    private Boolean isActive;
}