package com.alex.cryptotracker.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public void sendAlertEmail(String to, String coinId, Double price) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject("🔥 ALERTA CRYPTO: " + coinId.toUpperCase() + " 🔥");

            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 500px; margin: auto;'>"
                    + "<h2 style='color: #d9534f; text-align: center;'>⚠️ Alerta Crypto!</h2>"
                    + "<p style='font-size: 16px;'>Salut,</p>"
                    + "<p style='font-size: 16px;'>Moneda <b>" + coinId.toUpperCase() + "</b> tocmai a atins pragul setat de tine.</p>"
                    + "<div style='background-color: #f9f9f9; padding: 15px; text-align: center; border-radius: 5px; margin: 20px 0;'>"
                    + "<span style='font-size: 14px; color: #555;'>Preț curent:</span><br>"
                    + "<span style='font-size: 28px; font-weight: bold; color: #28a745;'>$" + price + "</span>"
                    + "</div>"
                    + "<p style='font-size: 14px; color: #777; text-align: center;'>Baftă la tranzacții!</p>"
                    + "</div>";

            helper.setText(htmlContent, true);

            log.info("Se incearca conexiunea SMTP pentru mailul HTML catre {}...", to);
            mailSender.send(message);
            log.info("SUCCES: Mailul HTML a fost trimis catre {} pentru moneda {}", to, coinId);

        } catch (Exception e) {
            log.error("EROARE FATALA LA TRIMITERE MAIL HTML catre {}: {}", to, e.getMessage(), e);
        }
    }
}