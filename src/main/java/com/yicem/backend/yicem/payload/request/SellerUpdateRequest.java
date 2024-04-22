package com.yicem.backend.yicem.payload.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerUpdateRequest {

    //Seller attributes
    @Size(max = 100)
    private String address;
    @Size(max = 10)
    private String phone;
    @Size(max = 20)
    private String businessName;
    private String openingHour;
    private String closingHour;
    private String locationLatitude;
    private String locationLongitude;
    private String logo;
    private float reservationTimeout;
}
