package com.alex.cryptotracker.service;

import com.alex.cryptotracker.model.CryptoAlert;
import com.alex.cryptotracker.repository.CryptoAlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Value("${coingecko.api.key}")
    private String apiKey;

    public CryptoPriceService(CryptoAlertRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
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
            log.warn("Nu s-au putut prelua preturile de la API. Se sare peste acest ciclu.");
            return;
        }

        for (CryptoAlert alert : activeAlerts) {
            Double currentPrice = currentPrices.get(alert.getCoinId());

            if (currentPrice != null && currentPrice <= alert.getTargetPrice()) {
                log.info("ALERTA: {} a scazut la {}. Se notifica {}", alert.getCoinId(), currentPrice, alert.getUserEmail());

                emailService.sendAlertEmail(alert.getUserEmail(), alert.getCoinId(), currentPrice);

                alert.setIsActive(false);
                repository.save(alert);
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