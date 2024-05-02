package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.models.*;
import com.yicem.backend.yicem.payload.request.PasswordChangeRequest;
import com.yicem.backend.yicem.payload.request.ReviewRequest;
import com.yicem.backend.yicem.payload.response.MessageResponse;
import com.yicem.backend.yicem.payload.response.ReservationResponse;
import com.yicem.backend.yicem.payload.response.SellerResponse;
import com.yicem.backend.yicem.payload.response.TransactionResponse;
import com.yicem.backend.yicem.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

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
        String buyerId = userService.getIdFromHeader(header);

        Optional<Buyer> buyerOptional = buyerRepository.findById(buyerId);
        if(buyerOptional.isPresent()) { // Buyer exists in DB
            Buyer buyer = buyerOptional.get();

            Optional<Offer> offerInstance = offerRepository.findById(offerId);
            if(offerInstance.isPresent()){
                Offer offer = offerInstance.get();

                //TODO: Change reservation structure, change timeslot with pickuptime
                if(offer.getItemCount() > 0) { // Such reservation DNE in DB

                    Reservation newReservation = new Reservation(buyerId, offer.getSellerId(), offerId, timeSlot);
                    reservationRepository.save(newReservation);

                    // Add new reservation's ID to the related models
                    offer.addReservation(newReservation.getId());
                    buyer.addReservation(newReservation.getId());

                    offerRepository.save(offer);
                    buyerRepository.save(buyer);

                    return ResponseEntity.ok(new MessageResponse("Reservation made for " + timeSlot + " successfully"));
                }
                else { // This reservation already made and exists in DB
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new MessageResponse("There are no available offer left."));
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

    public ResponseEntity<?> cancelReservation(HttpHeaders header, String reservationId) {
        String buyerId = userService.getIdFromHeader(header);

        Optional<Buyer> buyerOptional = buyerRepository.findById(buyerId);
        if(buyerOptional.isPresent()) { // Buyer exists in DB
            Buyer buyer = buyerOptional.get();

            if(buyer.getActiveReservations().contains(reservationId)) { // Buyer has THIS reservation

                Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
                if(reservationOptional.isPresent()) { // reservationId exists in reservations table
                    Reservation reservation = reservationOptional.get();

                    buyer.removeReservation(reservationId);
                    buyerRepository.save(buyer);

                    Optional<Offer> offerOptional = offerRepository.findById(reservation.getOfferId());
                    if(offerOptional.isPresent()) { // Offer of this reservation exists
                        Offer offer = offerOptional.get();

                        offer.removeReservation(reservationId);
                        offerRepository.save(offer);

                        reservationRepository.deleteById(reservationId);
                        return ResponseEntity.ok(
                                new MessageResponse("Reservation:" + reservationId + " has been cancelled"));
                    } else { // Offer of this reservation DNE
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new MessageResponse("Offer:" + reservation.getOfferId() + " is not found"));
                    }
                } else { // reservationId DNE in reservations table
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new MessageResponse("Reservation:" + reservationId + " is not found in DB"));
                }
            } else { // Buyer does not have reservationId in its activeReservations list
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Buyer does not have reservation:" + reservationId + " in DB"));
            }

        } else { // buyerId DNE in buyers table
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }
    }

    public ResponseEntity<?> getPurchases(HttpHeaders header){
        String buyerId = userService.getIdFromHeader(header);

        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);

        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();

            List<TransactionResponse> responses = new ArrayList<>();

            List<Transaction> transactions = transactionRepository.findAllById(buyer.getPastTransactions());
            for (Transaction transaction : transactions) {
                TransactionResponse transactionResponse = new TransactionResponse(transaction);

                transactionResponse.setBuyerName(buyer.getUsername());

                Optional<Seller> sellerOptional = sellerRepository.findById(transaction.getSellerId());
                sellerOptional.ifPresent(seller -> transactionResponse.setSellerName(seller.getBusinessName()));

                Optional<Offer> offerOptional = offerRepository.findById(transaction.getOfferId());
                offerOptional.ifPresent(offer -> transactionResponse.setOfferName(offer.getOfferName()));

                Optional<Review> reviewOptional = reviewRepository.findById(transaction.getReview());
                reviewOptional.ifPresent(transactionResponse::setReview);

                responses.add(transactionResponse);

            }
            return ResponseEntity.ok(responses);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }
    }

    public ResponseEntity<?> changeUsername(HttpHeaders header, String newUsername){
        String userId = userService.getIdFromHeader(header);

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

        String userId = userService.getIdFromHeader(header);
        if(userId.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: There is no valid token"));
        }

        Optional<Buyer> buyerOptional = buyerRepository.findById(userId);

        if(buyerOptional.isPresent()){
            return userService.changePassword(header, passwordChangeRequest);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Buyer is not found");
        }
    }

    public ResponseEntity<?> getFavorites(HttpHeaders header){
        String buyerId = userService.getIdFromHeader(header);

        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);

        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();
            List<Seller> sellers = sellerRepository.findAllById(buyer.getFavoriteSellers());
            List<SellerResponse> res = sellerService.getSellersFromList(sellers);
            return ResponseEntity.ok(res);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }
    }

    public ResponseEntity<?> addToFavorites(HttpHeaders header, String businessId) {
        String buyerId = userService.getIdFromHeader(header);

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

    public ResponseEntity<?> removeFromFavorites(HttpHeaders header, String businessId) {
        String buyerId = userService.getIdFromHeader(header);

        Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);
        if(buyerInstance.isPresent()){
            Buyer buyer = buyerInstance.get();

            if (sellerRepository.findById(businessId).isPresent()){

                if( buyer.removeFavoriteSeller(businessId) ) {
                    buyerRepository.save(buyer);
                    return ResponseEntity.ok(new MessageResponse("Seller is removed from favorites"));
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new MessageResponse("Error: Not in favorites"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Seller is not found"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }
    }

    public ResponseEntity<?> reviewBusiness(HttpHeaders header, String transactionId, ReviewRequest reviewRequest){

        String buyerId = userService.getIdFromHeader(header);
        if(buyerId.isEmpty()){
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

                            String comment = reviewRequest.getComment();
                            int rating = reviewRequest.getRating();

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
        String buyerId = userService.getIdFromHeader(header);

        Optional<Buyer> buyerOptional = buyerRepository.findById(buyerId);
        if(buyerOptional.isPresent()) { // Buyer is in DB
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

    public ResponseEntity<?> getActiveReservations(HttpHeaders header) {
        String buyerId = userService.getIdFromHeader(header);

        Optional<Buyer> buyerOptional = buyerRepository.findById(buyerId);
        if(buyerOptional.isPresent()){ // buyerId exists in Buyers table
            Buyer buyer = buyerOptional.get();

            List<String> reservationIds = buyer.getActiveReservations();
            List<Reservation> reservations = reservationRepository.findAllById(reservationIds);
            List<ReservationResponse> responseList = new ArrayList<>();
            for(Reservation reservation : reservations){
                String reservationId = reservation.getId();
                String timeslot, sellerName, offerName;
                float offerPrice;

                Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
                if(reservationOptional.isPresent()){
                    timeslot = reservationOptional.get().getTimeSlot();
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new MessageResponse("Reservation is not found"));
                }

                Optional<Seller> sellerOptional = sellerRepository.findById(reservation.getSellerId());
                if(sellerOptional.isPresent()){
                    sellerName = sellerOptional.get().getBusinessName();
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Seller is not found"));
                }

                Optional<Offer> offerOptional = offerRepository.findById(reservation.getOfferId());
                if(offerOptional.isPresent()){
                    Offer offer = offerOptional.get();
                    offerName = offer.getOfferName();
                    offerPrice = offer.getPrice();
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Offer is not found"));
                }

                ReservationResponse response = new ReservationResponse(reservationId, sellerName, buyer.getUsername(),
                        offerName, offerPrice, timeslot);
                responseList.add(response);
            }

            return ResponseEntity.ok(responseList);
        } else { // buyerId DNE in buyers
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
        }

    }
}
