package com.alex.cryptotracker.controller;

import com.alex.cryptotracker.dto.CryptoAlertRequest;
import com.alex.cryptotracker.dto.CryptoAlertResponse;
import com.alex.cryptotracker.dto.NotificationHistoryResponse;
import com.alex.cryptotracker.model.CryptoAlert;
import com.alex.cryptotracker.model.NotificationHistory;
import com.alex.cryptotracker.repository.CryptoAlertRepository;
import com.alex.cryptotracker.repository.NotificationHistoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alerts")
public class CryptoAlertController {

    private final CryptoAlertRepository repository;
    private final NotificationHistoryRepository historyRepository;

    public CryptoAlertController(CryptoAlertRepository repository, NotificationHistoryRepository notificationHistoryRepository, NotificationHistoryRepository historyRepository) {
        this.repository = repository;
        this.historyRepository = historyRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CryptoAlertResponse createAlert(@Valid @RequestBody CryptoAlertRequest request) {
        CryptoAlert alert = new CryptoAlert();
        alert.setCoinId(request.getCoinId().toLowerCase());
        alert.setTargetPrice(request.getTargetPrice());
        alert.setUserEmail(request.getUserEmail());
        alert.setDirection(request.getDirection());
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
        response.setDirection(alert.getDirection()); // Trimitem directia inapoi catre UI
        return response;
    }
    @GetMapping("/history")
    public List<NotificationHistoryResponse> getNotificationHistory() {
        return historyRepository.findAll().stream()
                .map(this::mapToHistoryResponse)
                .collect(Collectors.toList());
    }

    private NotificationHistoryResponse mapToHistoryResponse(NotificationHistory history) {
        NotificationHistoryResponse response = new NotificationHistoryResponse();
        response.setId(history.getId());
        response.setUserEmail(history.getUserEmail());
        response.setCoinId(history.getCoinId());
        response.setPriceAtNotification(history.getPriceAtNotification());
        response.setDirection(history.getDirection());
        response.setSentAt(history.getSentAt());
        response.setStatus(history.getStatus());
        return response;
    }
}