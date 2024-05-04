package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.models.Seller;
import com.yicem.backend.yicem.payload.response.MessageResponse;
import com.yicem.backend.yicem.repositories.BuyerRepository;
import com.yicem.backend.yicem.repositories.ReviewRepository;
import com.yicem.backend.yicem.repositories.SellerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminService {
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public ResponseEntity<?> approveSellers(String sellerId){
        Optional<Seller> sellerInstance = sellerRepository.findById(sellerId);

        if(sellerInstance.isPresent()){
            Seller seller = sellerInstance.get();
            seller.setApproved(true);

            return ResponseEntity.ok("Seller is approved");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Seller is not found"));
        }
    }

    /*public ResponseEntity<?> deleteSeller(String sellerId){

    }

    public ResponseEntity<?> deleteBuyer(String buyerId){

    }


    public ResponseEntity<?> deleteReview(String reviewId){

    }*/
}
