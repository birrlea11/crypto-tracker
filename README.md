# 🚀 Crypto Tracker API

[![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen?style=for-the-badge&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)](https://www.mysql.com/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203.0-dfd?style=for-the-badge&logo=swagger)](http://swagger.io/)

**Crypto Tracker API** este un sistem backend performant construit cu Spring Boot 3.4.3, creat pentru a monitoriza prețurile criptomonedelor și a trimite alerte automate prin email atunci când pragurile setate sunt atinse.

---

## ✨ Caracteristici

* **🔍 Monitorizare Batch**: Preia prețurile de la CoinGecko API într-un singur apel pentru toate monedele urmărite activ, optimizând resursele.
* **⏰ Verificare Automatizată**: Sistemul verifică prețurile în mod constant la fiecare 60 de secunde (fixed rate).
* **📧 Notificări Inteligente**: Trimite email-uri asincrone (`@Async`) cu design HTML adaptiv (verde pentru creștere, roșu pentru scădere).
* **📜 Istoric Complet**: Fiecare notificare trimisă cu succes este salvată în baza de date pentru consultări ulterioare.
* **🛠️ Robustete**: Include un handler global pentru excepții care asigură răspunsuri clare în caz de erori de validare.

---

## 🛠️ Stack Tehnologic

* **Framework**: Spring Boot 3.4.3
* **Bază de date**: MySQL (via Spring Data JPA)
* **Documentație**: SpringDoc OpenAPI (Swagger)
* **Utilități**: Project Lombok
* **Email**: Spring Mail / Brevo

---

## 📐 Arhitectura

Aplicația este organizată modular:
1.  **Controller**: Expune endpoint-urile REST pentru gestionarea alertelor și vizualizarea istoricului.
2.  **Service**: Conține logica de business pentru verificarea prețurilor (`CryptoPriceService`) și trimiterea mail-urilor (`EmailService`).
3.  **Repository**: Interfețe JpaRepository pentru accesul rapid la datele din MySQL.
4.  **Config**: Configurații pentru documentația OpenAPI și integrarea Swagger.

---

## 🚀 Instalare și Configurare

### 1. Prerechizite
* Java 17 sau mai nou.
* Maven instalat.
* O bază de date MySQL configurată.

### 2. Configurare `application.properties`
Adaugă următoarele proprietăți necesare:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crypto_tracker
spring.datasource.username=username_ul_tau
spring.datasource.password=parola_ta

coingecko.api.key=cheia_ta_api
brevo.sender.email=nume@domeniu.ro
