package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.models.Buyer;
import com.yicem.backend.yicem.models.Offer;
import com.yicem.backend.yicem.models.Reservation;
import com.yicem.backend.yicem.models.Seller;
import com.yicem.backend.yicem.models.Transaction;
import com.yicem.backend.yicem.repositories.BuyerRepository;
import com.yicem.backend.yicem.repositories.OfferRepository;
import com.yicem.backend.yicem.repositories.SellerRepository;
import com.yicem.backend.yicem.repositories.TransactionRepository;
import com.yicem.backend.yicem.repositories.UserRepository;
import com.yicem.backend.yicem.security.services.SellerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
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
    private SellerService sellerService;

    @GetMapping("/all")
    public List<Seller> getSellers() {
        return sellerService.getSellers();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable String id){
        return sellerService.deleteSeller(id);
    }

    @PostMapping("/{seller}/addOffer")
    public ResponseEntity<?> addOffer(@RequestBody Offer offer, @PathVariable("seller") String sellerID) {
        return sellerService.addOffer(offer, sellerID);
    }
    
    @PostMapping("/{seller}/modifyOffer/{offerID}")
    public ResponseEntity<?> modifyOffer(@RequestBody Offer offerInfo, @PathVariable("seller") String sellerID,
     @PathVariable("offerID") String offerID) {
        return sellerService.modifyOffer(offerInfo, sellerID, offerID);
    }

    @DeleteMapping("/{seller}/modifyOffer/{offerID}/deleteOffer")
    public ResponseEntity<?> deleteOffer(@PathVariable("seller") String sellerID,
     @PathVariable("offerID") String offerID) {
        return sellerService.deleteOffer(sellerID, offerID);
    }
    
    @GetMapping("/{seller}/offers")
    public ResponseEntity<?> getOffers(@PathVariable("seller") String sellerID) {
        return sellerService.getOffers(sellerID);
    }

    @PostMapping("/{seller}/markSold/{offerID}")
    public ResponseEntity<?> markOfferSold(@PathVariable("seller") String sellerID,
    @PathVariable("offerID") String offerID) {
        return sellerService.markOfferSold(sellerID, offerID);
    }
    
    


}