package com.yicem.backend.yicem.payload.request;

import com.yicem.backend.yicem.helpers.PickupTime;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OfferRequest {
    private String offerName;
    private String description;

    private float price;
    private int itemCount;

    private boolean isMysteryBox;

    private List<String> categories;
    private List<PickupTime> pickupTimes;
}
