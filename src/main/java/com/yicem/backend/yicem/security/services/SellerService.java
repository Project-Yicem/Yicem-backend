package com.yicem.backend.yicem.security.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

@Service
public class SellerService {
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

    public List<Seller> getSellers(){
        return sellerRepository.findAll();
    }

    public ResponseEntity<?> deleteSeller(String id){
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

    public ResponseEntity<?> addOffer(Offer offer, String sellerID){
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
            Offer newOffer = new Offer(offer);
            // save newoffer to generate unique id
            offerRepository.save(newOffer);
            // add new offer id to seller's offer list
            seller.getCurrentOffers().add(newOffer.getId());
            // save seller to update database
            sellerRepository.save(seller);
            return new ResponseEntity<String>("Offer added successfully.",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Seller not found.", HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> modifyOffer(Offer offerInfo, String sellerID, String offerID){
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

    public ResponseEntity<?> deleteOffer(String sellerID, String offerID){
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

    public ResponseEntity<?> getOffers(String sellerID){
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

    public ResponseEntity<?> markOfferSold(String sellerID, String offerID){
        System.out.println("Inside the method.");
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
                            Transaction transaction = new Transaction(buyer.getId(),
                            seller.getId(), offer.getOfferName(), offer.getPrice());
                            // add new transaction to the database
                            transactionRepository.save(transaction);
                            // create past transaction list if it is null
                            if (seller.getPastTransactions() == null) {
                                seller.setPastTransactions(new ArrayList<String>());
                            }
                            // update seller's past transaction list
                            seller.getPastTransactions().add(transaction.getId());
                            // update offer's queue
                            reservations.remove(0);
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
                            System.err.println("Item count was not valid after decrementing: " + offer.getItemCount());
                            return new ResponseEntity<>("Item count was not valid after decrementing.", HttpStatus.CONFLICT);
                        }
                    }
                    else{
                        System.err.println("First buyer from the queue does not exist.");
                        return new ResponseEntity<>("First buyer from the queue does not exist.", HttpStatus.BAD_REQUEST);
                    }
                }
                else{
                    System.err.println("No reservations have been made for this item yet.");
                    return new ResponseEntity<>("No reservations have been made for this item yet.", HttpStatus.EXPECTATION_FAILED);
                }
            }
            else{
                System.err.println("Specified offer not found.");
                return new ResponseEntity<>("Specified offer not found.", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            System.err.println("Specified seller not found.");
            return new ResponseEntity<>("Specified seller not found.", HttpStatus.BAD_REQUEST);
        }
    }
}
