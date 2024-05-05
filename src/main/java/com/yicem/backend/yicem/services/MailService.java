package com.yicem.backend.yicem.services;

import org.springframework.stereotype.Service;

import com.yicem.backend.yicem.models.User;
import com.yicem.backend.yicem.models.Buyer;
import com.yicem.backend.yicem.models.Seller;
import com.yicem.backend.yicem.repositories.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@Service
public class MailService {
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UserRepository userRepository;

    public void sendMessage(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
}
