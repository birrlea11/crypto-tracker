# 🚀 Crypto Alert System v2.0 (Advanced Edition)
**Backend System for Real-Time Financial Monitoring & Automated Notifications**

Sistem de monitorizare high-performance construit cu **Spring Boot 3.4.3**, optimizat pentru procesare asincronă și trasabilitate totală a datelor.

---

## ✨ Ce am adăugat nou (Pro Features)

* **⚡ Arhitectură Asincronă (@Async):** Trimiterea email-urilor nu mai blochează firul principal de execuție. Sistemul „aruncă” notificările într-un pool de thread-uri separat, asigurând o scalabilitate masivă.
* **🧠 Smart Tracking (ABOVE/BELOW):** Utilizatorul nu mai setează doar un preț; el definește o strategie (Buy the Dip sau Take Profit) prin parametrul `AlertDirection`.
* **📜 Sistem de Audit & History:** Orice notificare trimisă lasă o amprentă în tabelul `NotificationHistory`. Știm exact ce preț a fost în momentul X, la ce oră s-a trimis mailul și dacă a fost cu succes.
* **🎨 Frontend Modern "Dark Mode":** Interfață web integrată (HTML5/JS) servită direct de Spring Boot, eliminând nevoia de Postman pentru utilizatorul final.
* **📧 Dynamic HTML Templating:** Email-uri vizuale care își schimbă culoarea (Verde/Roșu) și subiectul în funcție de evoluția pieței.

---

## 🛠️ Stack Tehnologic Complet

* **Core:** Java 23, Spring Boot 3.4.3
* **Data Layer:** MySQL, Spring Data JPA, Hibernate
* **Concurrency:** Spring Task Scheduling (`@Scheduled`) & Async Executions (`@Async`)
* **Security:** Environment Variables Encapsulation, DTO Pattern (Request/Response separation)
* **Communication:** SMTP via Brevo API, MimeMessage HTML Templating
* **UI:** Lightweight Vanilla JS & CSS3 (Root-served)

---

## 🏗️ Arhitectura Datelor

### Smart Alert Logic
Sistemul folosește un flux de validare dublu în `checkPrices`:
1.  **Batch Fetching:** Extrage prețurile o singură dată pentru toate ID-urile unice (ex: 1 apel API pentru 100 de utilizatori de Bitcoin).
2.  **Directional Check:** * `ABOVE`: `currentPrice >= targetPrice` (Profit Strategy)
    * `BELOW`: `currentPrice <= targetPrice` (Entry Strategy)

### Entități de Bază:
* `CryptoAlert`: Stochează pragul, direcția și starea (Active/Inactive).
* `NotificationHistory`: Log-ul permanent de execuție pentru audit.

---

## 🚦 Endpoint-uri API (Swagger Enabled)

Accesează: `http://localhost:8080/swagger-ui/index.html`

| Metodă | Path | Descriere |
| :--- | :--- | :--- |
| **POST** | `/api/alerts` | Creează alertă cu direcție (`ABOVE`/`BELOW`) |
| **GET** | `/api/alerts` | Lista alertelor active |
| **GET** | `/api/alerts/history` | Istoricul notificărilor trimise (Audit Trail) |

---

## ⚙️ Variabile de Mediu (Security First)

Aplicația **nu** rulează fără următoarele chei configurate în IDE:

| Variabilă | Rol |
| :--- | :--- |
| `DB_URL` / `DB_PASSWORD` | Conexiune MySQL Persistență |
| `COINGECKO_API_KEY` | Acces Date Real-Time |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | Credențiale SMTP (Brevo) |

---

## 🎨 Interfața Utilizator
Sistemul include un Frontend integrat accesibil direct la:
`http://localhost:8080/`

---

👨‍💻 **Autor:** Alex (Fullstack Java Developer)
