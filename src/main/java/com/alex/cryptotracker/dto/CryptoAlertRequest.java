package com.alex.cryptotracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CryptoAlertRequest {

    @NotBlank
    private String coinId;

    @Positive
    private Double targetPrice;

    @NotBlank
    @Email
    private String userEmail;
}