package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.models.Offer;
import com.yicem.backend.yicem.models.Seller;
import com.yicem.backend.yicem.repositories.BuyerRepository;
import com.yicem.backend.yicem.repositories.SellerRepository;
import com.yicem.backend.yicem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuyerService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private SellerRepository sellerRepository;

    public BuyerService(UserRepository uRepository, BuyerRepository bRepository, SellerRepository sRepository){
        this.userRepository = uRepository;
        this.buyerRepository = bRepository;
        this.sellerRepository = sRepository;
    }

    public List<Seller> listAllApproved(){
        return sellerRepository.findByIsApproved(true);
    }

    public ResponseEntity<Object> listAllOffersOfBusiness(String businessId){
        Optional<Seller> sellerInstance = sellerRepository.findById(businessId);
        if(sellerInstance.isPresent()){
            Seller seller = sellerInstance.get();
            return ResponseEntity.ok(seller.getCurrentOffers());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business is not found");
        }

    }
}
