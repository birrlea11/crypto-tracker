package com.alex.cryptotracker.controller;

import com.alex.cryptotracker.dto.CryptoAlertRequest;
import com.alex.cryptotracker.dto.CryptoAlertResponse;
import com.alex.cryptotracker.model.CryptoAlert;
import com.alex.cryptotracker.repository.CryptoAlertRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alerts")
public class CryptoAlertController {

    private final CryptoAlertRepository repository;

    public CryptoAlertController(CryptoAlertRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CryptoAlertResponse createAlert(@Valid @RequestBody CryptoAlertRequest request) {
        CryptoAlert alert = new CryptoAlert();
        alert.setCoinId(request.getCoinId().toLowerCase());
        alert.setTargetPrice(request.getTargetPrice());
        alert.setUserEmail(request.getUserEmail());
        alert.setIsActive(true);

        CryptoAlert savedAlert = repository.save(alert);

        return mapToResponse(savedAlert);
    }

    @GetMapping
    public List<CryptoAlertResponse> getAllAlerts() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CryptoAlertResponse mapToResponse(CryptoAlert alert) {
        CryptoAlertResponse response = new CryptoAlertResponse();
        response.setId(alert.getId());
        response.setCoinId(alert.getCoinId());
        response.setTargetPrice(alert.getTargetPrice());
        response.setIsActive(alert.getIsActive());
        return response;
    }
}