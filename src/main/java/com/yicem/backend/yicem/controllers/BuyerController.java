package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.models.Buyer;
import com.yicem.backend.yicem.models.Offer;
import com.yicem.backend.yicem.models.Seller;
import com.yicem.backend.yicem.repositories.BuyerRepository;
import com.yicem.backend.yicem.repositories.SellerRepository;
import com.yicem.backend.yicem.repositories.UserRepository;
import com.yicem.backend.yicem.services.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    private final BuyerService buyerService;

    @GetMapping("/all")
    public List<Buyer> getBuyers() {
        return buyerRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBuyer(@PathVariable String id){
        if (buyerRepository.existsById(id)) {
            buyerRepository.deleteById(id);
            userRepository.deleteById(id);
            return "Deleted buyer";
        }

        return "Buyer does not exist";
    }

    @GetMapping("/businesses")
    public List<Seller> getSellers(){
        return buyerService.listAllApproved();
    }

    @GetMapping("/business/{businessId}/offers")
    public ResponseEntity<Object> getBusinessesOffers(@PathVariable String businessId){
        return buyerService.listAllOffersOfBusiness(businessId);
    }


    //@GetMapping("/business/{businessId}/reviews")


    //@PostMapping("/business/{businessId}/report")


    //@PostMapping("/business/{businessId}/{offerId}/reserve")

    /*
        +GET /customer-api/businesses: Backend sends the list of all APPROVED businesses (not the unapproved businesses!!!). A business has fields: name, logo, opening time, closing time, location (latitude-longitude), rating, address (text), phone number etc.

        GET /customer-api/business/{businessId}/offers: Frontend gets the current offers of a business with {businessId}. An offer has the following fields: name, price, list of available hours to pick up the offer, add more fields as needed

        GET /customer-api/business/{businessId}/reviews: Frontend gets the reviews of a business with {businessId}. A review has a text, username, date and a rating.

        POST /customer-api/business/{businessId}/report: Frontend posts a report to backend. A report is associated with a business (with {businessId}). A report has a text, date and a username.

        POST /customer-api/business/{businessId}/{offerId}/reserve: Reserve the offer {offerId} of the business {businessId} with a picked timeslot. What is sent to backend: timeslot, username.

        POST /customer-api/user/update/: Update a field (password or username) of the user that is logged in. Frontend sends the JWT token along with the field to be updated. (Backend should except two fields, password and username, but only one of the fields will be full. For example, if the user updates username, the password field will be empty/null and vice versa).

        GET /customer-api/user/purchases: Backend returns the list of purchases of the user. A purchase has the following fields: business name, purchased item name, date, price

        POST /customer-api/user/{purchaseId}/review: Frontend posts a review. A review has fields: text, date, username, rating and the name of the business that the review is associated with.

        POST /customer-api/user/favorite/{businessId}: Add the business with {businessId} to the user’s favorites.

        GET /customer-api/user/favorites/: Get the user’s favorite businesses.

        POST /customer-api/user/register: Register a user. The fields that are sent are: username, email, password
     */

}
