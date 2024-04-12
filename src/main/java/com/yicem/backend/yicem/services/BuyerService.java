package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.models.*;
import com.yicem.backend.yicem.payload.response.MessageResponse;
import com.yicem.backend.yicem.repositories.*;
import com.yicem.backend.yicem.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BuyerService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private JwtUtils jwtUtils;

    private String parseJwt(HttpHeaders header) {
        if(header.get("Authorization") != null){
            String token = header.get("Authorization").get(0);

            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                return token.substring(7, token.length());
            }
        }

        //TODO add error handling mechanism for null tokens
        return null;
    }

    public List<Seller> listAllApproved(){
        return sellerRepository.findByIsApproved(true);
    }

    //checks whether the business with given id exists, if business exits then returns the list of offer id's
    public ResponseEntity<Object> listAllOfferIdsOfBusiness(String businessId){
        Optional<Seller> sellerInstance = sellerRepository.findById(businessId);
        if(sellerInstance.isPresent()){
            Seller seller = sellerInstance.get();
            //TODO write helper methods for getting offers from ids
            return ResponseEntity.ok(seller.getCurrentOffers());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business is not found");
        }

    }

    public ResponseEntity<Object> listAllReviewIdsOfBusiness(String businessId){
        Optional<Seller> sellerInstance = sellerRepository.findById(businessId);
        if(sellerInstance.isPresent()){
            Seller seller = sellerInstance.get();
            //TODO write helper methods for getting reviews from ids
            return ResponseEntity.ok(seller.getReviews());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business is not found");
        }
    }

    public ResponseEntity<Object> reserveTheOffer(HttpHeaders header, String offerId, String timeSlot){
        String token = parseJwt(header);
        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: There is no valid token"));
        }

        String buyerId = jwtUtils.getIdFromJwtToken(token);
        Optional<Offer> offerInstance = offerRepository.findById(offerId);

        if(offerInstance.isPresent()){
            Offer offer = offerInstance.get();
            Reservation newReservation = new Reservation();
            newReservation.setBuyerId(buyerId);
            newReservation.setSellerId(offer.getSellerId());
            newReservation.setTimeSlot(timeSlot);
            offer.getReservations().add(newReservation);
            reservationRepository.save(newReservation);
            offerRepository.save(offer);

            return ResponseEntity.ok("Reservation made for " + timeSlot + " successfully.");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Offer is not found");
        }
    }

    public ResponseEntity<?> getPurchases(String buyerId){
        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);

        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();

            return ResponseEntity.ok(buyer.getPastTransactions());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Buyer is not found");
        }
    }

    public ResponseEntity<?> changeUsername(User user, String newUsername){
        user.setUsername(newUsername);

        userRepository.save(user);

        return ResponseEntity.ok("Username changed");
    }

    public ResponseEntity<?> changePassword(User user, String newPassword){
        user.setPassword(newPassword);

        userRepository.save(user);

        return ResponseEntity.ok("Password changed");
    }

    public ResponseEntity<?> getFavorites(String buyerId){
        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);

        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();

            return ResponseEntity.ok(buyer.getFavoriteSellers());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Buyer is not found");
        }
    }

    public ResponseEntity<?> addToFavorites(String buyerId, String businessId){
        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);

        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();

            if (sellerRepository.findById(businessId).isPresent()){
                Seller favoritedSeller = sellerRepository.findById(businessId).get();

                if(buyer.getFavoriteSellers() == null){
                    List<Seller> newList = new ArrayList<>();
                    newList.add(favoritedSeller);
                    buyer.setFavoriteSellers(newList);
                }
                else{
                    buyer.getFavoriteSellers().add(favoritedSeller);
                }
                buyerRepository.save(buyer);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller is not found");
            }

            return ResponseEntity.ok("Restaurant is added to favorites");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Buyer is not found");
        }
    }
}
