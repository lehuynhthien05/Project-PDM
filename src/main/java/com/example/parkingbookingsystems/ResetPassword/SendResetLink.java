package com.example.parkingbookingsystems.ResetPassword;

import jakarta.mail.Session;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class SendResetLink {

    public void sendResetLink(String contact, String token) {
        String resetLink = "http://yourapp.com/reset_password?token=" + token;
        String to = contact; // recipient email
        String from = "your-email@gmail.com"; // sender email
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your-email@gmail.com", "your-email-password");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Password Reset Request");
            message.setText("Click the link to reset your password: " + resetLink);

            Transport.send(message);
            System.out.println("Reset link sent successfully...");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}