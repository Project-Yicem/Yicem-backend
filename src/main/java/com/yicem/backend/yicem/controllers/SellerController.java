package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.models.Offer;
import com.yicem.backend.yicem.models.Seller;
import com.yicem.backend.yicem.repositories.OfferRepository;
import com.yicem.backend.yicem.repositories.SellerRepository;
import com.yicem.backend.yicem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private OfferRepository offerRepository;

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


    @PostMapping("/{seller}/addOffer")
    public ResponseEntity addOffer(@RequestBody Offer offer, @PathVariable("seller") String sellerID) {
        // hold seller optional
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);
        // check its existence
        if (sellerOptional.isPresent()) {
            // put in Seller type variable to be able to save new state to database after operations
            Seller seller = sellerOptional.get();
            // Create the list if it is null
            if (seller.getCurrentOffers() == null) {
                seller.setCurrentOffers(new ArrayList<String>());
            }
            // add offer id and save seller
            seller.getCurrentOffers().add(offer.getId());
            sellerRepository.save(seller);
            return new ResponseEntity<String>("Offer added successfully.",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Seller not found.", HttpStatus.BAD_REQUEST);
        }         
    }
    
    

    @PostMapping("/{seller}/modifyOffer")
    public String modifyOffer(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
    @GetMapping("/{seller}/offers")
    public List<String> getOffers(@PathVariable("seller") String sellerUserName) {
        if (sellerRepository.existsByUsername(sellerUserName)) {
            return sellerRepository.findByUsername(sellerUserName).get().getCurrentOffers();
        }
        else{
            System.err.println("A seller does not exist with the given username");
            return null;
        }
    }
    


}