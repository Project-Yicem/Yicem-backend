package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.models.*;
import com.yicem.backend.yicem.payload.response.MessageResponse;
import com.yicem.backend.yicem.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminService {
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    public ResponseEntity<?> approveSellers(HttpHeaders header, String sellerId) {
        String adminId = userService.getIdFromHeader(header);

        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (adminOptional.isPresent()) {
            Optional<Seller> sellerInstance = sellerRepository.findById(sellerId);

            if (sellerInstance.isPresent()) {
                Seller seller = sellerInstance.get();
                seller.setApproved(true);
                sellerRepository.save(seller);
                return ResponseEntity.ok("Seller is approved");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Seller is not found"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Admin is not found"));
        }
    }

    public ResponseEntity<?> deleteSeller(HttpHeaders header, String sellerId) {
        String adminId = userService.getIdFromHeader(header);

        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (adminOptional.isPresent()) {
            return sellerService.deleteSeller(sellerId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Admin is not found"));
        }
    }

    public ResponseEntity<?> deleteBuyer(HttpHeaders header, String buyerId) {
        String adminId = userService.getIdFromHeader(header);

        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (adminOptional.isPresent()) {
            Optional<Buyer> buyerInstance = buyerRepository.findById(buyerId);

            if (buyerInstance.isPresent()) {
                Buyer buyer = buyerInstance.get();
                List<String> buyersActiveReservations = buyer.getActiveReservations();
                //Delete reservations of buyer and remove from offers active reservation list
                for (String reservationId : buyersActiveReservations) {
                    Reservation reservation = reservationRepository.findById(reservationId).get();
                    Offer offer = offerRepository.findById(reservation.getOfferId()).get();
                    offer.removeReservation(reservationId);
                    offerRepository.save(offer);
                    reservationRepository.deleteById(reservationId);
                }
                buyerRepository.deleteById(buyerId);
                userRepository.deleteById(buyerId);

                return ResponseEntity.ok("Buyer is deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Buyer is not found"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Admin is not found"));
        }
    }


    public ResponseEntity<?> deleteReview(HttpHeaders header, String reviewId) {
        String adminId = userService.getIdFromHeader(header);

        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (adminOptional.isPresent()) {
            Optional<Review> reviewInstance = reviewRepository.findById(reviewId);

            if (reviewInstance.isPresent()) {
                Review review = reviewInstance.get();
                String transactionId = review.getTransactionId();

                if (transactionRepository.findById(transactionId).isPresent()) {
                    Transaction transaction = transactionRepository.findById(transactionId).get();
                    String sellerId = transaction.getSellerId();
                    String buyerId = transaction.getBuyerId();

                    if (sellerRepository.findById(sellerId).isPresent()) { //Remove review id from the seller if the seller is present
                        Seller seller = sellerRepository.findById(sellerId).get();
                        List<String> sellersReviews = seller.getReviews();
                        seller.updateReviewRating(review.getRating());
                        sellersReviews.remove(reviewId);
                        seller.setReviews(sellersReviews);
                        sellerRepository.save(seller);
                    }

                    if (buyerRepository.findById(buyerId).isPresent()) { //Remove review id from the buyer if the buyer is present
                        Buyer buyer = buyerRepository.findById(buyerId).get();
                        List<String> buyersReviews = buyer.getReviews();
                        buyersReviews.remove(reviewId);

                        buyer.setReviews(buyersReviews);
                        buyerRepository.save(buyer);
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Transaction is not found"));
                }
                reviewRepository.deleteById(reviewId);

                return ResponseEntity.ok("Review deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Review is not found"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Admin is not found"));
        }
    }
}
