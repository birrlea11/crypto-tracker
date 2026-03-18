package com.alex.cryptotracker.repository;

import com.alex.cryptotracker.model.CryptoAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoAlertRepository extends JpaRepository<CryptoAlert, Long> {
}