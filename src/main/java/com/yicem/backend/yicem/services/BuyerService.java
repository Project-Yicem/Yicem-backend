package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.models.*;
import com.yicem.backend.yicem.payload.request.PasswordChangeRequest;
import com.yicem.backend.yicem.payload.request.ReviewRequest;
import com.yicem.backend.yicem.payload.response.MessageResponse;
import com.yicem.backend.yicem.payload.response.SellerResponse;
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
    @Autowired
    private SellerService sellerService;

    private String getIdFromHeader(HttpHeaders header) {
        if(header.get("Authorization") != null){
            String token = header.get("Authorization").get(0);

            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7, token.length());
                return jwtUtils.getIdFromJwtToken(jwtToken);
            }
        }

        return "";
    }

    public List<SellerResponse> listAllApproved(){
        return sellerService.getApprovedSellers();
    }

    // Checks whether the business with given id exists. If business exists, then returns the list of offerIDs.
    public ResponseEntity<Object> listAllOfferIdsOfBusiness(String businessId) {
        Optional<Seller> sellerInstance = sellerRepository.findById(businessId);
        if (sellerInstance.isPresent()) {
            Seller seller = sellerInstance.get();
            List<Offer> res = offerRepository.findAllById(seller.getOffers());
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Business is not found"));
        }

    }

    public ResponseEntity<?> getReviewsOfBusiness(String businessId){
        Optional<Seller> sellerInstance = sellerRepository.findById(businessId);
        if(sellerInstance.isPresent()){
            Seller seller = sellerInstance.get();
            List<Review> reviews = reviewRepository.findAllById(seller.getReviews());
            return ResponseEntity.ok(reviews);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Business is not found"));
        }
    }

    public ResponseEntity<Object> reserveTheOffer(HttpHeaders header, String offerId, String timeSlot){
        String buyerId = getIdFromHeader(header);

        Optional<Buyer> buyerOptional = buyerRepository.findById(buyerId);
        if(buyerOptional.isPresent()) { // Buyer exists in DB

            Optional<Offer> offerInstance = offerRepository.findById(offerId);
            if(offerInstance.isPresent()){
                Offer offer = offerInstance.get();

                if(!reservationRepository.existsByBuyerIdAndOfferId(buyerId, offerId)) { // Such reservation DNE in DB

                    Reservation newReservation = new Reservation(buyerId, offer.getSellerId(), offerId, timeSlot);
                    reservationRepository.save(newReservation);

                    offer.addReservation(newReservation.getId());
                    offerRepository.save(offer);

                    return ResponseEntity.ok(new MessageResponse("Reservation made for " + timeSlot + " successfully"));
                }
                else { // This reservation already made and exists in DB
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new MessageResponse("Reservation already exists"));
                }
            }
            else { // Offer is not in DB
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Offer is not found"));
            }

        }
        else { // Buyer is not in DB
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }

    }

    public ResponseEntity<?> getPurchases(HttpHeaders header){
        String buyerId = getIdFromHeader(header);

        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);

        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();
            List<Transaction> res = transactionRepository.findAllById(buyer.getPastTransactions());
            return ResponseEntity.ok(res);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }
    }

    public ResponseEntity<?> changeUsername(HttpHeaders header, String newUsername){
        String userId = getIdFromHeader(header);

        Optional<User> userInstance = userRepository.findById(userId);
        Optional<Buyer> buyerInstance = buyerRepository.findById(userId);

        if(userInstance.isPresent() && buyerInstance.isPresent()) {
            User user = userInstance.get();
            Buyer buyer = buyerInstance.get();

            user.setUsername(newUsername);
            buyer.setUsername(newUsername);

            userRepository.save(user);
            buyerRepository.save(buyer);

            return ResponseEntity.ok(new MessageResponse("Username changed"));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User is not found"));
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
        String buyerId = getIdFromHeader(header);

        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);

        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();
            List<Seller> res = sellerRepository.findAllById(buyer.getFavoriteSellers());
            return ResponseEntity.ok(res);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }
    }

    public ResponseEntity<?> addToFavorites(HttpHeaders header, String businessId) {
        String buyerId = getIdFromHeader(header);

        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);
        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();

            if (sellerRepository.findById(businessId).isPresent()){ // Seller exists in DB

                if( buyer.addFavoriteSeller(businessId) ) {
                    buyerRepository.save(buyer);
                    return ResponseEntity.ok(new MessageResponse("Restaurant is added to favorites"));
                }
                else {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new MessageResponse("Error: Already exists in favorites"));
                }

            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Seller is not found"));
            }

        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }

    }

    public ResponseEntity<?> reviewBusiness(HttpHeaders header, String transactionId, ReviewRequest reviewRequest){

        String comment = reviewRequest.getComment();
        int rating = reviewRequest.getRating();

        String buyerId = getIdFromHeader(header);
        if(buyerId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: There is no valid token"));
        }

        Optional<Buyer> buyerOptional = buyerRepository.findById(buyerId);
        if(buyerOptional.isPresent()){ // Such Buyer exists in DB
            Buyer buyer = buyerOptional.get();

            if (buyer.getPastTransactions().contains(transactionId)) { // Buyer has this Transaction in pastTransactions

                Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
                if(transactionOptional.isPresent()){ // Such Transaction exists in DB
                    Transaction transaction = transactionOptional.get();

                    if(transaction.getReview().isEmpty()) { // Is not reviewed yet.

                        Optional<Seller> sellerOptional = sellerRepository.findById(transaction.getSellerId());
                        if(sellerOptional.isPresent()){ // Such seller exists in DB -> ALL CONDITIONS ARE OK FOR PROCESS
                            Seller seller = sellerOptional.get();

                            Review review = new Review(transactionId, comment, rating);
                            reviewRepository.save(review);

                            buyer.addReview(review.getId());
                            seller.addReview(review.getId(), review.getRating());
                            transaction.setReview(review.getId());

                            buyerRepository.save(buyer);
                            sellerRepository.save(seller);
                            transactionRepository.save(transaction);

                            return ResponseEntity.ok(new MessageResponse("Review added successfully"));
                        }
                        else {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(new MessageResponse("Seller is not found"));
                        }
                    }
                    else {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(new MessageResponse("This is already reviewed"));
                    }

                }
                else { // This Transaction does not exist in DB
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new MessageResponse("Transaction is not found"));
                }

            }
            else { // This Buyer does not have such transaction.
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Buyer does not have such transaction."));
            }

        }
        else { // This Buyer does not exist.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }

    }

    public ResponseEntity<?> reportBusiness(HttpHeaders header, String businessId, String reportDesc) {
        String buyerId = getIdFromHeader(header);

        Optional<Buyer> buyerOptional = buyerRepository.findById(buyerId);
        if(buyerOptional.isPresent()){

            Buyer buyer = buyerOptional.get();

            Report report = new Report(businessId, reportDesc);
            reportRepository.save(report);

            buyer.addSupportReport(report.getId());
            buyerRepository.save(buyer);

            return ResponseEntity.ok(new MessageResponse("Business is reported successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }

    }
}
