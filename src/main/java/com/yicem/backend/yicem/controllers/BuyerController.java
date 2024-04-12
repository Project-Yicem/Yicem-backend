package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.models.Buyer;
import com.yicem.backend.yicem.models.Offer;
import com.yicem.backend.yicem.models.Seller;
import com.yicem.backend.yicem.repositories.BuyerRepository;
import com.yicem.backend.yicem.repositories.SellerRepository;
import com.yicem.backend.yicem.repositories.UserRepository;
import com.yicem.backend.yicem.services.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    private final BuyerService buyerService;

    /*@PutMapping("/update-username")
    public ResponseEntity<?> updateUsername(@RequestBody String newUsername){
        return buyerService.changeUsername(newUsername);
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody String newPassword){
        return buyerService.changePassword(newPassword);
    }*/

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

    @GetMapping("/view-businesses")
    public List<Seller> getSellers(){
        return buyerService.listAllApproved();
    }

    @GetMapping("/business/{businessId}/offers")
    public ResponseEntity<Object> getBusinessesOffers(@PathVariable String businessId){
        return buyerService.listAllOfferIdsOfBusiness(businessId);
    }


    @GetMapping("/business/{businessId}/reviews")
    public ResponseEntity<Object> getBusinessReviews(@PathVariable String businessId){
        return buyerService.listAllReviewIdsOfBusiness(businessId);
    }

    //TODO add report endpoint
    //@PostMapping("/business/{businessId}/report")


    @PostMapping("/{buyerId}/{businessId}/{offerId}/reserve")
    public ResponseEntity<Object> reserveTheOffer(@PathVariable String buyerId, @PathVariable String businessId, @PathVariable String offerId, @RequestParam String timeSlot){
        return buyerService.reserveTheOffer(buyerId, businessId, offerId, timeSlot);
    }


    @GetMapping("/{buyerId}/purchases")
    public ResponseEntity<?> getPurchases(@PathVariable String buyerId){
        return buyerService.getPurchases(buyerId);
    }

    //TODO add review endpoint
    /*@PostMapping("/{buyerId}/{transactionId}/review")*/

    @PostMapping("/{buyerId}/favorite/{businessId}")
    public ResponseEntity<?> addFavorite(@PathVariable String buyerId, @PathVariable String businessId){
        return buyerService.addToFavorites(buyerId, businessId);
    }

    @GetMapping("/{buyerId}/favorites")
    public ResponseEntity<?> getFavorites(@PathVariable String buyerId){
        return buyerService.getFavorites(buyerId);
    }

}
