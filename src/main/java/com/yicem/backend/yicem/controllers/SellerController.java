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
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/all")
    public List<Seller> getSellers() {
        return sellerRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable String id){
        Optional<Seller> optionalSeller = sellerRepository.findById(id);
        if (optionalSeller.isPresent()) {
            Seller seller = optionalSeller.get();
            // delete offers of seller
            List<String> currentOffers = seller.getCurrentOffers();
            for (String offerID : currentOffers) {
                offerRepository.deleteById(offerID);
            }
            //TODO: delete reviews of seller
            //TODO: delete pastTransactions of seller
            // delete seller
            sellerRepository.deleteById(id);
            userRepository.deleteById(id);
            return new ResponseEntity<>("Deleted seller", HttpStatus.OK);
            
        }

        return new ResponseEntity<>("Seller does not exist", HttpStatus.OK);
    }


    @PostMapping("/{seller}/addOffer")
    public ResponseEntity<?> addOffer(@RequestBody Offer offer, @PathVariable("seller") String sellerID) {
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
            //TODO: generate unique id for offer
            // add offer id and save seller
            seller.getCurrentOffers().add(offer.getId());
            sellerRepository.save(seller);
            offerRepository.save(offer);
            return new ResponseEntity<String>("Offer added successfully.",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Seller not found.", HttpStatus.BAD_REQUEST);
        }         
    }
    
    

    @PostMapping("/{seller}/modifyOffer/{offerID}")
    public ResponseEntity<?> modifyOffer(@RequestBody Offer offerInfo, @PathVariable("seller") String sellerID,
     @PathVariable("offerID") String offerID) {
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);
        //Validate seller existence
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();
            Optional<Offer> offerOptional = offerRepository.findById(offerID);
            // validate offer existence
            if (offerOptional.isPresent()) {
                Offer offer = offerOptional.get();
                offer.updateInfo(offerInfo);
                offerRepository.save(offer);
                return new ResponseEntity<>("Offer successfully updated.", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Specified offer not found.", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>("Specified seller not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{seller}/modifyOffer/{offerID}/deleteOffer")
    public ResponseEntity<?> deleteOffer(@PathVariable("seller") String sellerID,
     @PathVariable("offerID") String offerID) {
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);
        // Validate seller existence
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();
            Optional<Offer> offerOptional = offerRepository.findById(offerID);
            // validate offer existence
            if (offerOptional.isPresent()) {
                offerRepository.deleteById(offerID);
                // update seller's current offer list and save to database
                seller.getCurrentOffers().remove(offerID);
                sellerRepository.save(seller);
                return new ResponseEntity<>("Offer successfully deleted.", HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Specified offer not found.", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>("Specified seller not found.", HttpStatus.BAD_REQUEST);
        }
    }
    
    
    
    @GetMapping("/{seller}/offers")
    public ResponseEntity<?> getOffers(@PathVariable("seller") String sellerID) {
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);
        //Validate seller existence
        if (sellerOptional.isPresent()) {
            
            Seller seller = sellerOptional.get();
            
            if (seller.getCurrentOffers() != null) {
                                
                List<String> currentOfferIDs = seller.getCurrentOffers();
                List<Offer> currentOffers = new ArrayList<Offer>();
                
                for (String offerID : currentOfferIDs) {
                    Optional<Offer> optionalOffer = offerRepository.findById(offerID);
                    if (optionalOffer.isPresent()) {
                        Offer offer = optionalOffer.get();
                        currentOffers.add(offer);
                    }
                }    

                return new ResponseEntity<>(currentOffers, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Offer list is empty.", HttpStatus.OK);
            }
        }       
        else{
            return new ResponseEntity<>("Specified seller does not exist.", HttpStatus.OK);
        }
    }

    @PostMapping("/{seller}/markSold/{offerID}")
    public ResponseEntity<?> markOfferSold(@PathVariable("seller") String sellerID,
    @PathVariable("offerID") String offerID) {
        
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);
        // Validate seller existence
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();
            Optional<Offer> offerOptional = offerRepository.findById(offerID);
            // validate offer existence
            if (offerOptional.isPresent()) {
                Offer offer = offerOptional.get();
                // validate reservation exists
                List<Reservation> reservations = offer.getReservations();
                if (reservations != null) {
                    Optional<Buyer> buyerOptional = buyerRepository.findById(reservations.get(0).getId());
                    // validate buyer exists
                    if (buyerOptional.isPresent()) {
                        Buyer buyer = buyerOptional.get();
                        // update offer count
                        if(offer.decrementItemCount()){
                            // create new transaction object
                            //TODO: generate random ID for transaction
                            Transaction transaction = new Transaction("123", buyer.getId(),
                            seller.getId(), offer.getOfferName(), offer.getPrice(), null, new Date());
                            // add new transaction to the database
                            transactionRepository.save(transaction);
                            // create past transaction list if it is null
                            if (seller.getPastTransactions() == null) {
                                seller.setPastTransactions(new ArrayList<String>());
                            }
                            // update seller's past transaction list
                            seller.getPastTransactions().add(transaction.getId());
                            // check if there are still offers available
                            if (offer.isCompleted()) {
                                // no more offers, have to delete offer from seller's current offers list
                                seller.getCurrentOffers().remove(offer.getId());
                                sellerRepository.save(seller);
                                offerRepository.save(offer);
                                return new ResponseEntity<>("Last of the offer successfully sold.", HttpStatus.OK);
                            }
                            else{
                                // there are more offers available
                                offerRepository.save(offer);
                                return new ResponseEntity<>("Available offer count successfully decremented.", HttpStatus.OK);
                            }
                        }
                        else{
                            return new ResponseEntity<>("Item count was not valid after decrementing.", HttpStatus.CONFLICT);
                        }
                    }
                    else{
                        return new ResponseEntity<>("First buyer from the queue does not exist.", HttpStatus.BAD_REQUEST);
                    }
                }
                else{
                    return new ResponseEntity<>("No reservations have been made for this item yet.", HttpStatus.EXPECTATION_FAILED);
                }
            }
            else{
                return new ResponseEntity<>("Specified offer not found.", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>("Specified seller not found.", HttpStatus.BAD_REQUEST);
        }
    }
    
    


}