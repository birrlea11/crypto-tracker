# 🚀 Crypto Alert System

Sistem de backend performant construit cu **Spring Boot**, creat pentru a monitoriza prețurile criptomonedelor și a trimite alerte automate prin email.

---

## 🛠️ Stack Tehnologic

* **Backend:** Java 23, Spring Boot 3.4.3
* **Bază de date:** MySQL (Spring Data JPA)
* **Validare:** Jakarta Validation API
* **Documentație:** Swagger UI & OpenAPI 3 (v2.8.5)
* **Utilitare:** Lombok, SLF4J (Logging), RestTemplate

---

## ✨ Funcționalități Cheie

* **Arhitectură Layered:** Structură curată (Controller -> Service -> Repository).
* **Securitate prin DTO-uri:** Utilizarea `CryptoAlertRequest` și `CryptoAlertResponse` pentru a proteja entitățile bazei de date.
* **Validare Automată:** Verificarea email-urilor și a prețurilor pozitive direct la intrarea în API.
* **Global Exception Handling:** Răspunsuri JSON customizate pentru erori (HTTP 400), evitând erorile generice de sistem.
* **Monitorizare Automată:** Task-uri programate (`@Scheduled`) care verifică prețurile prin CoinGecko API la fiecare minut.
* **Alerte Email HTML:** Notificări vizuale trimise via SMTP (Brevo).

---

## 🚦 Ghid de Utilizare (API)

Interfața **Swagger** este disponibilă la:  
`http://localhost:8080/swagger-ui/index.html`

### Endpoint-uri principale:
* `POST /api/alerts` : Creează o alertă nouă (necesită `coinId`, `targetPrice`, `userEmail`).
* `GET /api/alerts` : Listează toate alertele monitorizate.

---

## ⚙️ Configurare Proiect

Aplicația folosește variabile de mediu pentru securitate. Configurează următoarele în mediul tău de rulare (IDE):

| Variabilă | Descriere |
| :--- | :--- |
| `DB_URL` | URL-ul conexiunii MySQL |
| `DB_USERNAME` | Username bază de date |
| `DB_PASSWORD` | Parola bază de date |
| `COINGECKO_API_KEY` | API Key CoinGecko |
| `MAIL_USERNAME` | SMTP Username (Brevo) |
| `MAIL_PASSWORD` | SMTP Password (Brevo) |
| `MAIL_SENDER` | Adresa email expeditor |

---

👨‍💻 **Autor:** Alex
