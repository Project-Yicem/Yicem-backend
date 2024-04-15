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
import org.springframework.http.HttpHeaders;
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

    @PutMapping("/update-username")
    public ResponseEntity<?> updateUsername(@RequestHeader HttpHeaders header, @RequestBody String newUsername){
        return buyerService.changeUsername(header, newUsername);
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestHeader HttpHeaders header, @RequestBody String newPassword){
        return buyerService.changePassword(header, newPassword);
    }

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

    @PostMapping("/business/{businessId}/report")
    public ResponseEntity<?> reportTheBusiness(@PathVariable String businessId, @RequestParam String reportDescription){
        return buyerService.reportBusiness(businessId, reportDescription);
    }


    @PostMapping("/reserve/{offerId}")
    public ResponseEntity<Object> reserveTheOffer(@RequestHeader HttpHeaders header, @PathVariable String offerId, @RequestParam String timeSlot){
        return buyerService.reserveTheOffer(header, offerId, timeSlot);
    }


    @GetMapping("/purchases")
    public ResponseEntity<?> getPurchases(@RequestHeader HttpHeaders header){
        return buyerService.getPurchases(header);
    }

    @PostMapping("/{transactionId}/review")
    public ResponseEntity<?> reviewTheBusiness(@RequestHeader HttpHeaders header, @PathVariable String transactionId, @RequestParam String comment, @RequestParam float rating){
        return buyerService.reviewBusiness(header, transactionId, comment, rating);
    }


    @PostMapping("/favorite/{businessId}")
    public ResponseEntity<?> addFavorite(@RequestHeader HttpHeaders header, @PathVariable String businessId){
        return buyerService.addToFavorites(header, businessId);
    }

    @GetMapping("/favorites")
    public ResponseEntity<?> getFavorites(@RequestHeader HttpHeaders header){
        return buyerService.getFavorites(header);
    }

}
