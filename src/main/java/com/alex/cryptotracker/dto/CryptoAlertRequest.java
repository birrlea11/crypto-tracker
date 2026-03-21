package com.alex.cryptotracker.dto;

import com.alex.cryptotracker.model.AlertDirection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Direction is required (ABOVE or BELOW)")
    private AlertDirection direction;
}