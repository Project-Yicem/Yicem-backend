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
import com.yicem.backend.yicem.security.jwt.JwtUtils;
import com.yicem.backend.yicem.security.services.SellerService;
import com.yicem.backend.yicem.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;





@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/seller")
@PreAuthorize("hasRole('ROLE_SELLER') || hasRole('ROLE_ADMIN')")
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
        if (checkSellerAuthenticaton(sellerID)) {
            return sellerService.addOffer(offer, sellerID);    
        }
        else{
            return new ResponseEntity<>("Access denied to the specified object.", HttpStatus.FORBIDDEN);
        }
    }
    
    @PostMapping("/{seller}/modifyOffer/{offerID}")
    public ResponseEntity<?> modifyOffer(@RequestBody Offer offerInfo, @PathVariable("seller") String sellerID,
     @PathVariable("offerID") String offerID) {
        if (checkSellerAuthenticaton(sellerID)) {
            return sellerService.modifyOffer(offerInfo, sellerID, offerID);
        }
        else{
            return new ResponseEntity<>("Access denied to the specified object.", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{seller}/modifyOffer/{offerID}/deleteOffer")
    public ResponseEntity<?> deleteOffer(@PathVariable("seller") String sellerID,
     @PathVariable("offerID") String offerID) {
        if (checkSellerAuthenticaton(sellerID)) {
            return sellerService.deleteOffer(sellerID, offerID);    
        }
        else{
            return new ResponseEntity<>("Access denied to the specified object.", HttpStatus.FORBIDDEN);
        }
    }
    
    @GetMapping("/{seller}/offers")
    public ResponseEntity<?> getOffers(@PathVariable("seller") String sellerID) {
        if (checkSellerAuthenticaton(sellerID)) {
            return sellerService.getOffers(sellerID);    
        }
        else{
            return new ResponseEntity<>("Access denied to the specified object.", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/{seller}/markSold/{offerID}")
    public ResponseEntity<?> markOfferSold(@PathVariable("seller") String sellerID,
    @PathVariable("offerID") String offerID) {
        if (checkSellerAuthenticaton(sellerID)) {
            return sellerService.markOfferSold(sellerID, offerID);    
        }
        else{
            return new ResponseEntity<>("Access denied to the specified object.", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("{seller}/history")
    public ResponseEntity<?> getHistory(@PathVariable("seller") String sellerID) {
        if (checkSellerAuthenticaton(sellerID)) {
            return sellerService.getHistory(sellerID);    
        }
        else{
            return new ResponseEntity<>("Access denied to the specified object.", HttpStatus.FORBIDDEN);
        }
        
    }
    
    

    @GetMapping("{seller}/test")
    public ResponseEntity<?> testMethod(@PathVariable("seller") String sellerID) {
        
        checkSellerAuthenticaton(sellerID);

        return new ResponseEntity<>("exited method successfully.", HttpStatus.OK);
    }

    /**
     * Compares the object id from the JWT token with the id given within the request
     * @param sellerID the id given within the request, (specified id)
     * @return true if ids match, false otherwise
     */
    private boolean checkSellerAuthenticaton(String sellerID){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //System.out.println("principal: " + authentication.getPrincipal());
        //System.out.println("credentials: " + authentication.getCredentials()); 
        //System.out.println("name: " + authentication.getName());
        //System.out.println("the object itself: " + authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        System.out.println("object id from jwt token: " + userDetails.getId());
        if (sellerID.equals(userDetails.getId())) {
            return true;
        }
        else{
            return false;
        }
    }
    

}