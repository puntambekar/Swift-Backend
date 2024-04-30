package com.sportify.swift.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    //GRYFB5N7S1UFKQW43E547KC8
    @Autowired
    JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String htmlContent) {

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("koohi.bhagya@gmail.com");
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);

        MimeMessage message = mailSender.createMimeMessage();


        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("koohi.bhagya@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Set HTML content
        } catch (MessagingException e) {
            e.printStackTrace();
        }


        mailSender.send(message);
    }
}
