package com.yicem.backend.yicem.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.yicem.backend.yicem.models.Buyer;
import com.yicem.backend.yicem.models.Seller;
import com.yicem.backend.yicem.models.User;
import com.yicem.backend.yicem.repositories.UserRepository;
import com.yicem.backend.yicem.services.MailService;

@Controller
public class MailController {

    @Autowired
    MailService mailService;

    @Autowired
    UserRepository userRepository;

    public void sendSellerRegistrationMail(Seller seller){
        Optional<User> userOptional = userRepository.findById(seller.getId());
        if (userOptional.isPresent()) {
            User sellerUser = userOptional.get();
            String bodyText = "You have successfully registered as a business owner to Yicem! Our team will review your application and activate your account as soon as possible.";
            mailService.sendMessage(sellerUser.getEmail(), "Welcome to Yicem", bodyText);
        }
        else{
            System.err.println("No user exists which matches with seller object's ID");
        }
    }

    public void sendBuyerRegistrationMail(Buyer buyer){
        Optional<User> userOptional = userRepository.findById(buyer.getId());
        if (userOptional.isPresent()) {
            User buyerUser = userOptional.get();
            String bodyText = "You have successfully registered to Yicem as a customer.";
            mailService.sendMessage(buyerUser.getEmail(), "Welcome to Yicem", bodyText);
        }
        else{
            System.err.println("No user exists which matches with buyer object's ID");
        }
    }

    public void sendSellerVerifiedMail(Seller seller){
        Optional<User> userOptional = userRepository.findById(seller.getId());
        if (userOptional.isPresent()) {
            User sellerUser = userOptional.get();
            String bodyText = "Your application has been reviewed and accepted. You can start to use the Yicem application with all of its capabilities now!";
            mailService.sendMessage(sellerUser.getEmail(), "Business verification complete", bodyText);
        }
        else{
            System.err.println("No user exists which matches with seller object's ID");
        }
    }

    public void mailTest(){
        // enter your email instead of placeholder during testing
        mailService.sendMessage("placeholder", "Mail Test", "Test mail body");
    }
    
}
