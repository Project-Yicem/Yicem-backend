package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.models.Seller;
import com.yicem.backend.yicem.repositories.SellerRepository;
import com.yicem.backend.yicem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @GetMapping("/all")
    public List<Seller> getSellers() {
        return sellerRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSeller(@PathVariable String id){
        if (sellerRepository.existsById(id)) {
            sellerRepository.deleteById(id);
            userRepository.deleteById(id);
            return "Deleted seller";
        }

        return "Seller does not exist";
    }



}