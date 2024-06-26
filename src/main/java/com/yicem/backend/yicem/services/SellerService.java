package com.yicem.backend.yicem.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.yicem.backend.yicem.models.*;
import com.yicem.backend.yicem.payload.request.OfferRequest;
import com.yicem.backend.yicem.payload.request.PasswordChangeRequest;
import com.yicem.backend.yicem.payload.request.SellerUpdateRequest;
import com.yicem.backend.yicem.payload.response.*;
import com.yicem.backend.yicem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private ReviewRepository reviewRepository;

    public List<SellerResponse> getSellers() {
        List<Seller> sellers = sellerRepository.findAll();
        return getSellersFromList(sellers);
    }

    public ResponseEntity<?> getSeller(String sellerId) {

        Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);
        LocalTime now = LocalTime.now();
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();

            SellerResponse sellerResponse = new SellerResponse(seller);
            LocalTime openingHour, closingHour;
            openingHour = LocalTime.parse(seller.getOpeningHour());
            closingHour = LocalTime.parse(seller.getClosingHour());
            sellerResponse.setOpen(now.isAfter(openingHour) && now.isBefore(closingHour));

            return ResponseEntity.ok(sellerResponse);

        } else {
            return new ResponseEntity<>("Seller not found", HttpStatus.NOT_FOUND);
        }
    }

    public List<SellerResponse> getApprovedSellers() {
        List<Seller> sellers = sellerRepository.findByIsApproved(true);
        return getSellersFromList(sellers);
    }

    public List<SellerResponse> getSellersFromList(List<Seller> sellers) {
        List<SellerResponse> sellerResponses = new ArrayList<>();
        LocalTime now = LocalTime.now();
        for (Seller seller : sellers) {
            SellerResponse sellerResponse = new SellerResponse(seller);
            LocalTime openingHour, closingHour;
            openingHour = LocalTime.parse(seller.getOpeningHour());
            closingHour = LocalTime.parse(seller.getClosingHour());
            sellerResponse.setOpen(now.isAfter(openingHour) && now.isBefore(closingHour));
            sellerResponses.add(sellerResponse);
        }
        return sellerResponses;
    }

    public ResponseEntity<?> deleteSeller(String id) {
        Optional<Seller> optionalSeller = sellerRepository.findById(id);
        if (optionalSeller.isPresent()) {
            Seller seller = optionalSeller.get();
            // delete offers of seller
            List<String> currentOffers = seller.getOffers();
            for (String offerID : currentOffers) {
                Offer offer = offerRepository.findById(offerID).get();
                List<String> activeReservations = offer.getReservations();
                // delete reservations from the offer and the buyer
                for (String reservationID : activeReservations) {
                    Reservation reservation = reservationRepository.findById(reservationID).get();
                    Buyer buyer = buyerRepository.findById(reservation.getBuyerId()).get();
                    buyer.removeReservation(reservationID);
                    buyerRepository.save(buyer);
                    reservationRepository.deleteById(reservationID);
                }
                offerRepository.deleteById(offerID);
            }
            //TODO: delete reviews of seller
            //TODO: delete pastTransactions of seller (?) probably not needed
            // delete seller
            sellerRepository.deleteById(id);
            userRepository.deleteById(id);
            return new ResponseEntity<>(new MessageResponse("Deleted seller"), HttpStatus.OK);

        }

        return new ResponseEntity<>(new MessageResponse("Seller does not exist"), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> addOffer(OfferRequest request, String sellerID) {
        // Hold seller optional
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);
        System.out.println(sellerOptional.get().getId());

        // Check its existence
        if (sellerOptional.isPresent()) {
            // Put in Seller type variable to be able to save new state to database after operations
            Seller seller = sellerOptional.get();

            Offer newOffer = new Offer(request, sellerID);

            // Save new offer to generate unique id
            offerRepository.save(newOffer);

            // Add new offer id to seller's offer list
            seller.addOffer(newOffer.getId());

            // Save seller to update database
            sellerRepository.save(seller);

            return new ResponseEntity<>(new MessageResponse("Offer added successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Seller not found.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> modifyOffer(OfferRequest request, String sellerID, String offerID) {

        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);

        // Validate seller existence
        if (sellerOptional.isPresent()) {

            Seller seller = sellerOptional.get();
            List<String> currentOffers = seller.getOffers();
            if (currentOffers.contains(offerID)) {

                Optional<Offer> offerOptional = offerRepository.findById(offerID);
                // Validate offer existence
                if (offerOptional.isPresent()) {

                    Offer offer = offerOptional.get();

                    offer.updateInfo(request);
                    offerRepository.save(offer);

                    return new ResponseEntity<>(new MessageResponse("Offer successfully updated"), HttpStatus.OK);

                } else {
                    return new ResponseEntity<>(new MessageResponse("Offer does not found"), HttpStatus.NOT_FOUND);
                }

            } else {
                return new ResponseEntity<>(new MessageResponse("Seller does not have such offer"), HttpStatus.NOT_FOUND);
            }

        } else {
            return new ResponseEntity<>(new MessageResponse("Specified seller not found"), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> deleteOffer(String sellerID, String offerID) {

        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);
        // Validate seller existence
        if (sellerOptional.isPresent()) {

            Seller seller = sellerOptional.get();
            List<String> currentOffers = seller.getOffers();
            if (currentOffers.contains(offerID)) {

                Optional<Offer> offerOptional = offerRepository.findById(offerID);
                // Validate offer existence
                if (offerOptional.isPresent()) {

                    // Update seller's current offer list and save to database
                    seller.removeOffer(offerID);
                    sellerRepository.save(seller);

                    List<Reservation> reservations = reservationRepository.findAllByOfferId(offerID);
                    for (Reservation reservation : reservations) {
                        String reservationID = reservation.getId();
                        Optional<Buyer> buyerOptional = buyerRepository.findByActiveReservationsContains(reservationID);
                        if (buyerOptional.isPresent()) {
                            Buyer buyer = buyerOptional.get();
                            buyer.removeReservation(reservationID);

                            buyerRepository.save(buyer);
                        }
                        reservationRepository.delete(reservation);
                    }

                    offerRepository.deleteById(offerID);

                    return new ResponseEntity<>(new MessageResponse("Offer successfully deleted"), HttpStatus.OK);

                } else {
                    return new ResponseEntity<>(new MessageResponse("Specified offer not found"), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("Seller does not have such offer"), HttpStatus.NOT_FOUND);
            }

        } else {
            return new ResponseEntity<>(new MessageResponse("Specified seller not found"), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> getOffers(String sellerID) {
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);
        //Validate seller existence
        if (sellerOptional.isPresent()) {

            Seller seller = sellerOptional.get();

            if (!seller.getOffers().isEmpty()) {

                List<String> currentOfferIDs = seller.getOffers();
                List<Offer> currentOffers = offerRepository.findAllById(currentOfferIDs);
                List<OfferResponse> responses = new ArrayList<>();

                for (Offer offer : currentOffers) {
                    OfferResponse offerResponse = new OfferResponse(offer);

                    List<Reservation> reservationList = reservationRepository.findAllById(offer.getReservations());
                    List<ReservationResponse> reservationResponses = new ArrayList<>();
                    for (Reservation reservation : reservationList) {
                        Optional<Buyer> buyerOptional = buyerRepository.findById(reservation.getBuyerId());
                        if (buyerOptional.isPresent()) {
                            Buyer buyer = buyerOptional.get();
                            reservationResponses.add( new ReservationResponse(reservation.getId(),
                                    seller.getBusinessName(), buyer.getUsername(), offer.getOfferName(),
                                    offer.getPrice(), reservation.getTimeSlot()));
                        }
                    }

                    offerResponse.setReservations(reservationResponses);

                    responses.add(offerResponse);
                }

                return new ResponseEntity<>(responses, HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new MessageResponse("Offer list is empty"), HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>(new MessageResponse("Specified seller does not exist"), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> markOfferSold(String sellerID, String offerID, String reservationID) {

        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);
        // Validate seller existence
        if (sellerOptional.isPresent()) { // Seller exists in DB

            Seller seller = sellerOptional.get();
            System.out.println("markOfferSold: Seller validated.");
            List<String> currentOfferIDs = seller.getOffers();

            if (currentOfferIDs.contains(offerID)) { // Seller has offer with OfferID
                Optional<Offer> offerOptional = offerRepository.findById(offerID);
                // Validate offer existence
                if (offerOptional.isPresent()) { // Offer exists in DB

                    Offer offer = offerOptional.get();
                    System.out.println("markOfferSold: Offer validated.");

                    Optional<Reservation> reservationOptional = reservationRepository.findById(reservationID);
                    // Validate reservation exists in database
                    if (reservationOptional.isPresent()) {
                        // validate reservation list is valid and specified reservation exists in offer's list
                        List<String> reservations = offer.getReservations();
                        if (!reservations.isEmpty() && reservations.contains(reservationID)) { // There IS at least one reservation
                            // get reservation from database since it is confirmed to exist in reservation list
                            Reservation reservation = reservationOptional.get();

                            Optional<Buyer> buyerOptional = buyerRepository.findById(reservation.getBuyerId());
                            if (buyerOptional.isPresent()) { // Buyer exists in DB
                                Buyer buyer = buyerOptional.get();
                                System.out.println("markOfferSold: Buyer validated.");

                                if (offer.sellItem()) { // Sell ONE item of the offer

                                    buyer.removeReservation(reservation.getId());
                                    reservationRepository.deleteById(reservation.getId());

                                    // Create new transaction object
                                    Transaction transaction = new Transaction(buyer.getId(), buyer.getUsername(),
                                            seller.getId(), seller.getBusinessName(), offer.getId(),
                                            offer.getOfferName(), offer.getPrice());
    
                                    // Add new transaction to the database
                                    transactionRepository.save(transaction);
                                    // Add new transaction ID to buyer and seller
                                    seller.addTransaction(transaction.getId());
                                    buyer.addTransaction(transaction.getId());

                                    // Update offer's queue
                                    System.out.println("Queue before item sold: " + offer.getReservations());
                                    offer.removeReservation(reservation.getId());
                                    System.out.println("Queue after item sold" + offer.getReservations());

                                    // Check if there are still offers available
                                    if (offer.isCompleted()) {

                                        // All items of this offer are sold. Delete this offer from seller.
                                        // TODO: Maybe create a PastOffers List of Seller and add it to this.
                                        //  Currently, it does NOT delete the offer from DB, just saves with 0 items.
                                        seller.removeOffer(offerID);

                                        sellerRepository.save(seller);
                                        offerRepository.save(offer);
                                        buyerRepository.save(buyer);

                                        return new ResponseEntity<>(new MessageResponse("Last of the offer successfully sold"),
                                                HttpStatus.OK);
                                    } else {
                                        // More items available
                                        offerRepository.save(offer);
                                        sellerRepository.save(seller);
                                        buyerRepository.save(buyer);
                                        return new ResponseEntity<>(new MessageResponse("Available offer count successfully decremented"),
                                                HttpStatus.OK);
                                    }

                                } else {
                                    System.err.println("Item count was not valid after decrementing: "
                                            + offer.getItemCount());
                                    return new ResponseEntity<>(new MessageResponse("Item count was not valid after decrementing"),
                                            HttpStatus.CONFLICT);
                                }

                            } else {
                                System.err.println("First buyer from the queue does not exist.");
                                return new ResponseEntity<>(new MessageResponse("First buyer from the queue does not exist"),
                                        HttpStatus.NOT_FOUND);
                            }

                        } else {
                            System.err.println("No reservations have been made for this item yet.");
                            System.err.println("Reservation queue: " + offer.getReservations());
                            return new ResponseEntity<>(new MessageResponse("No reservations have been made for this item yet"),
                                    HttpStatus.EXPECTATION_FAILED);
                        }
                    } else {
                        System.err.println("Specified reservation does not exist in database.");
                        return new ResponseEntity<>("Specified reservation does not exist in database.", HttpStatus.EXPECTATION_FAILED);
                    }

                } else {
                    System.err.println("Specified offer not found in database.");
                    return new ResponseEntity<>(new MessageResponse("Specified offer not found in database"), HttpStatus.NOT_FOUND);
                }

            } else {
                System.err.println("Specified offer not found in seller's offer list.");
                return new ResponseEntity<>(new MessageResponse("Specified offer not found in seller's offer list"), HttpStatus.NOT_FOUND);
            }

        } else {
            System.err.println("Specified seller not found.");
            return new ResponseEntity<>(new MessageResponse("Specified seller not found"), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> getHistory(String sellerID) {
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerID);
        //Validate seller existence
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();

            List<TransactionResponse> responses = new ArrayList<>();

            List<Transaction> transactions = transactionRepository.findAllById(seller.getPastTransactions());
            for (Transaction transaction : transactions) {
                TransactionResponse transactionResponse = new TransactionResponse(transaction);

                Optional<Review> reviewOptional = reviewRepository.findById(transaction.getReview());
                reviewOptional.ifPresent(transactionResponse::setReview);

                responses.add(transactionResponse);

            }
            return new ResponseEntity<>(responses, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new MessageResponse("Specified seller does not exist"), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> changeUsername(HttpHeaders header, String newUsername){
        String userId = userService.getIdFromHeader(header);

        Optional<User> userInstance = userRepository.findById(userId);
        Optional<Seller> sellerInstance = sellerRepository.findById(userId);

        if(userInstance.isPresent() && sellerInstance.isPresent()) {
            User user = userInstance.get();
            Seller seller = sellerInstance.get();

            user.setUsername(newUsername);
            seller.setUsername(newUsername);

            userRepository.save(user);
            sellerRepository.save(seller);

            return ResponseEntity.ok(new MessageResponse("Username changed"));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User is not found"));
        }
    }

    public ResponseEntity<?> changePassword(HttpHeaders header, PasswordChangeRequest passwordChangeRequest) {

        String userId = userService.getIdFromHeader(header);
        if (userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: There is no valid token"));
        }

        Optional<Seller> sellerOptional = sellerRepository.findById(userId);

        if (sellerOptional.isPresent()) {
            return userService.changePassword(header, passwordChangeRequest);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller is not found");
        }
    }

    public ResponseEntity<?> updateSeller(HttpHeaders header, SellerUpdateRequest updateRequest) {

        String userId = userService.getIdFromHeader(header);
        Optional<Seller> sellerOptional = sellerRepository.findById(userId);
        if (sellerOptional.isPresent()) {
            Seller seller = sellerOptional.get();

            // Update non-null values in request body
            if (updateRequest.getAddress() != null)
                seller.setAddress(updateRequest.getAddress());
            if (updateRequest.getPhone() != null)
                seller.setPhone(updateRequest.getPhone());
            if (updateRequest.getBusinessName() != null)
                seller.setBusinessName(updateRequest.getBusinessName());
            if (updateRequest.getOpeningHour() != null)
                seller.setOpeningHour(updateRequest.getOpeningHour());
            if (updateRequest.getClosingHour() != null)
                seller.setClosingHour(updateRequest.getClosingHour());
            if (updateRequest.getLocationLatitude() != null)
                seller.setLocationLatitude(updateRequest.getLocationLatitude());
            if (updateRequest.getLocationLongitude() != null)
                seller.setLocationLongitude(updateRequest.getLocationLongitude());
            if (updateRequest.getLogo() != null)
                seller.setLogo(updateRequest.getLogo());
            if (updateRequest.getReservationTimeout() != 0)
                seller.setReservationTimeout(updateRequest.getReservationTimeout());

            sellerRepository.save(seller);

            return ResponseEntity.ok("Seller updated successfully");

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller is not found");
        }
    }

}
