package com.alex.cryptotracker.service;

import com.alex.cryptotracker.model.AlertDirection;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async // Rulăm pe fundal, să nu înghețăm aplicația
    public void sendAlertEmail(String to, String coinId, Double price, AlertDirection direction) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Stabilim textul și culoarea în funcție de direcție
            String actionText = (direction == AlertDirection.ABOVE) ? "A CRESCUT PESTE" : "A SCĂZUT SUB";
            String color = (direction == AlertDirection.ABOVE) ? "#28a745" : "#d9534f"; // Verde pt sus, Roșu pt jos

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject("🚀 Alerta " + coinId.toUpperCase() + ": " + actionText);

            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 500px; margin: auto;'>"
                    + "<h2 style='color: " + color + "; text-align: center;'>🔔 Alertă de Preț!</h2>"
                    + "<p style='font-size: 16px; color: #333;'>Salutare,</p>"
                    + "<p style='font-size: 16px;'>Moneda <b>" + coinId.toUpperCase() + "</b> tocmai <b>" + actionText + "</b> pragul setat de tine.</p>"
                    + "<div style='background-color: #f9f9f9; padding: 20px; text-align: center; border-radius: 8px; margin: 20px 0; border-left: 5px solid " + color + ";'>"
                    + "<span style='font-size: 14px; color: #555;'>Preț actual detectat:</span><br>"
                    + "<span style='font-size: 32px; font-weight: bold; color: " + color + ";'>$" + String.format("%.2f", price) + "</span>"
                    + "</div>"
                    + "<p style='font-size: 14px; color: #777; text-align: center; border-top: 1px solid #eee; padding-top: 10px;'>"
                    + "Verifică acum portofoliul și ia o decizie! 🚀</p>"
                    + "</div>";

            helper.setText(htmlContent, true);

            log.info("Sarcina asincronă: Se trimite mail HTML către {} pentru {} (Direcție: {})", to, coinId, direction);
            mailSender.send(message);
            log.info("SUCCES: Notificare trimisă cu succes către {}", to);

        } catch (Exception e) {
            log.error("EROARE la trimiterea asincronă către {}: {}", to, e.getMessage());
        }
    }}