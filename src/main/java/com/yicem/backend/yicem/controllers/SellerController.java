package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.payload.request.OfferRequest;
import com.yicem.backend.yicem.payload.response.SellerResponse;
import com.yicem.backend.yicem.security.services.UserDetailsImpl;
import com.yicem.backend.yicem.services.SellerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/seller")
// Commented out for test purposes. It will uncommented after completion.
// @PreAuthorize("hasRole('ROLE_SELLER') || hasRole('ROLE_ADMIN') || hasRole('ROLE_BUYER')")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @GetMapping("/all")
    public List<SellerResponse> getSellers() {
        return sellerService.getSellers();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable String id){
        return sellerService.deleteSeller(id);
    }

    @PostMapping("/{seller}/addOffer")
    public ResponseEntity<?> addOffer(@RequestBody OfferRequest offerRequest, @PathVariable("seller") String sellerID) {
        if (checkSellerAuthenticaton(sellerID)) {
            return sellerService.addOffer(offerRequest, sellerID);
        }
        else {
            return new ResponseEntity<>("Access denied to the specified object.", HttpStatus.FORBIDDEN);
        }
    }
    
    @PostMapping("/{seller}/modifyOffer/{offerID}")
    public ResponseEntity<?> modifyOffer(@RequestBody OfferRequest request, @PathVariable("seller") String sellerID,
                                         @PathVariable("offerID") String offerID) {
        if (checkSellerAuthenticaton(sellerID)) {
            return sellerService.modifyOffer(request, sellerID, offerID);
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
        return sellerID.equals(userDetails.getId());
    }
    

}