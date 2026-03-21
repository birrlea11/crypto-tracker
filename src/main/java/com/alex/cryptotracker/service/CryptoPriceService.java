package com.alex.cryptotracker.service;

import com.alex.cryptotracker.model.AlertDirection;
import com.alex.cryptotracker.model.CryptoAlert;
import com.alex.cryptotracker.model.NotificationHistory;
import com.alex.cryptotracker.repository.CryptoAlertRepository;
import com.alex.cryptotracker.repository.NotificationHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CryptoPriceService {

    private final CryptoAlertRepository repository;
    private final EmailService emailService;
    private final RestTemplate restTemplate;
    private final NotificationHistoryRepository historyRepository;

    @Value("${coingecko.api.key}")
    private String apiKey;

    public CryptoPriceService(CryptoAlertRepository repository, EmailService emailService, NotificationHistoryRepository historyRepository) {
        this.repository = repository;
        this.emailService = emailService;
        this.historyRepository = historyRepository;
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(fixedRate = 60000)
    public void checkPrices() {
        List<CryptoAlert> activeAlerts = repository.findAll().stream()
                .filter(CryptoAlert::getIsActive)
                .toList();

        if (activeAlerts.isEmpty()) {
            return;
        }

        Set<String> uniqueCoinIds = activeAlerts.stream()
                .map(CryptoAlert::getCoinId)
                .collect(Collectors.toSet());

        Map<String, Double> currentPrices = fetchPricesInBatch(uniqueCoinIds);

        if (currentPrices.isEmpty()) {
            log.warn("Nu s-au putut prelua preturile de la API.");
            return;
        }

        for (CryptoAlert alert : activeAlerts) {
            Double currentPrice = currentPrices.get(alert.getCoinId());

            if (currentPrice != null) {
                boolean shouldNotify = false;

                if (alert.getDirection() == AlertDirection.ABOVE && currentPrice >= alert.getTargetPrice()) {
                    shouldNotify = true;
                } else if (alert.getDirection() == AlertDirection.BELOW && currentPrice <= alert.getTargetPrice()) {
                    shouldNotify = true;
                }

                if (shouldNotify) {
                    log.info("ALERTA: {} este la {}. (Prag: {} {}). Notificăm {}",
                            alert.getCoinId(), currentPrice, alert.getDirection(), alert.getTargetPrice(), alert.getUserEmail());

                    emailService.sendAlertEmail(alert.getUserEmail(), alert.getCoinId(), currentPrice,alert.getDirection());
                    NotificationHistory history = new NotificationHistory();
                    history.setUserEmail(alert.getUserEmail());
                    history.setCoinId(alert.getCoinId());
                    history.setPriceAtNotification(currentPrice);
                    history.setDirection(alert.getDirection());
                    history.setSentAt(LocalDateTime.now());
                    history.setStatus("SUCCESS");

                    historyRepository.save(history);
                    alert.setIsActive(false);
                    repository.save(alert);
                }
            }
        }
    }
    private Map<String, Double> fetchPricesInBatch(Set<String> coinIds) {
        String idsParam = String.join(",", coinIds);
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=" + idsParam + "&vs_currencies=usd";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cg-demo-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map<String, Map<String, Number>> responseBody = responseEntity.getBody();

            if (responseBody == null) {
                return Collections.emptyMap();
            }

            return responseBody.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().get("usd").doubleValue()
                    ));

        } catch (Exception e) {
            log.error("Eroare la apelul CoinGecko API: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}