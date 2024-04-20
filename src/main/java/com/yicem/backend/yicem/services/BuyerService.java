package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.models.*;
import com.yicem.backend.yicem.payload.request.PasswordChangeRequest;
import com.yicem.backend.yicem.payload.response.MessageResponse;
import com.yicem.backend.yicem.repositories.*;
import com.yicem.backend.yicem.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReportRepository reportRepository;

    private JwtUtils jwtUtils;

    private String parseJwt(HttpHeaders header) {
        if(header.get("Authorization") != null){
            String token = header.get("Authorization").get(0);

            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                return token.substring(7, token.length());
            }
        }

        return null;
    }

    private String getIdFromHeader(HttpHeaders header) {
        if(header.get("Authorization") != null){
            String token = header.get("Authorization").get(0);

            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7, token.length());
                return jwtUtils.getIdFromJwtToken(jwtToken);
            }
        }

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
            List<Offer> res = getOffersFromTheirIds(seller.getCurrentOffers());
            return ResponseEntity.ok(res);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business is not found");
        }

    }

    public List<Offer> getOffersFromTheirIds(List<String> ids){
        List<Offer> offerList = new ArrayList<>();
        for(String id : ids){
            Offer offer = offerRepository.findById(id).get();
            offerList.add(offer);
        }

        return offerList;
    }


    public ResponseEntity<Object> listAllReviewIdsOfBusiness(String businessId){
        Optional<Seller> sellerInstance = sellerRepository.findById(businessId);
        if(sellerInstance.isPresent()){
            Seller seller = sellerInstance.get();
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

    public ResponseEntity<?> getPurchases(HttpHeaders header){
        String token = parseJwt(header);
        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: There is no valid token"));
        }

        String buyerId = jwtUtils.getIdFromJwtToken(token);

        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);

        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();

            return ResponseEntity.ok(buyer.getPastTransactions());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Buyer is not found");
        }
    }

    public ResponseEntity<?> changeUsername(HttpHeaders header, String newUsername){
        String token = parseJwt(header);
        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: There is no valid token"));
        }

        String userId = jwtUtils.getIdFromJwtToken(token);

        Optional<User> userInstance = userRepository.findById(userId);
        Optional<Buyer> buyerInstance = buyerRepository.findById(userId);
        if(userInstance.isPresent()){
            User user = userInstance.get();
            Buyer buyer = buyerInstance.get();

            user.setUsername(newUsername);
            buyer.setUsername(newUsername);
            userRepository.save(user);
            buyerRepository.save(buyer);

            return ResponseEntity.ok("Username changed");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not found");
        }
    }

    public ResponseEntity<?> changePassword(HttpHeaders header, PasswordChangeRequest passwordChangeRequest) {

        String userId = getIdFromHeader(header);
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: There is no valid token"));
        }

        Optional<User> userInstance = userRepository.findById(userId);

        if(userInstance.isPresent()){
            User user = userInstance.get();
            String dbPassword = user.getPassword();
            String oldPassword = passwordChangeRequest.getOldPassword();
            String newPassword = passwordChangeRequest.getNewPassword();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(oldPassword, dbPassword)){
                newPassword = encoder.encode(newPassword);
                user.setPassword(newPassword);
                userRepository.save(user);
                return ResponseEntity.ok("Password changed");
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Error: Old password does not match"));
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not found");
        }
    }

    public ResponseEntity<?> getFavorites(HttpHeaders header){
        String token = parseJwt(header);
        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: There is no valid token"));
        }

        String buyerId = jwtUtils.getIdFromJwtToken(token);

        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);

        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();

            return ResponseEntity.ok(buyer.getFavoriteSellers());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Buyer is not found");
        }
    }

    public ResponseEntity<?> addToFavorites(HttpHeaders header, String businessId){
        String token = parseJwt(header);
        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: There is no valid token"));
        }

        String buyerId = jwtUtils.getIdFromJwtToken(token);

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

    public ResponseEntity<?> reviewBusiness(HttpHeaders header, String transactionId, String comment, float rating){
        String token = parseJwt(header);
        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: There is no valid token"));
        }
        String buyerId = jwtUtils.getIdFromJwtToken(token);

        Buyer buyer = buyerRepository.findById(buyerId).get();
        Transaction transaction = transactionRepository.findById(transactionId).get();
        Seller seller = sellerRepository.findById(transaction.getSellerId()).get();

        Review review = new Review();
        review.setTransactionId(transactionId);
        review.setComment(comment);
        review.setRating(rating);

        reviewRepository.save(review);

        if(buyer.getReviews() == null){
            List<Review> newList = new ArrayList<>();
            newList.add(review);
            buyer.setReviews(newList);
        }
        else {
            buyer.getReviews().add(review);
        }

        if(seller.getReviews() == null){
            List<Review> newList = new ArrayList<>();
            newList.add(review);
            seller.setReviews(newList);
        }
        else {
            seller.getReviews().add(review);
        }

        buyerRepository.save(buyer);
        sellerRepository.save(seller);

        return ResponseEntity.ok("Review has been made.");
    }

    public ResponseEntity<?> reportBusiness(String businessId, String reportDesc){
        Optional<Seller> sellerInstance = sellerRepository.findById(businessId);
        if(sellerInstance.isPresent()){
            Report report = new Report();
            report.setReportedBusinessId(businessId);
            report.setReportDescription(reportDesc);

            reportRepository.save(report);
            return ResponseEntity.ok("Business is reported");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business with given id is not found.");
        }
    }

}
