package com.alex.cryptotracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CryptoAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String coinId;
    private Double targetPrice;
    private String userEmail;
    private Boolean isActive = true;
}