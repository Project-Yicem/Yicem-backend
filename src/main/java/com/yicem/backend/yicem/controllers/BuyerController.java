package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.models.Buyer;
import com.yicem.backend.yicem.repositories.BuyerRepository;
import com.yicem.backend.yicem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/buyer")
public class BuyerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @GetMapping("/all")
    public List<Buyer> getBuyers() {
        return buyerRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBuyer(@PathVariable String id){
        if (buyerRepository.existsById(id)) {
            buyerRepository.deleteById(id);
            userRepository.deleteById(id);
            return "Deleted buyer";
        }

        return "Buyer does not exist";
    }



}
