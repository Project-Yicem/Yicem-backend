package com.yicem.backend.yicem.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponse {
    private String id;
    private String sellerName;
    private String buyerName;
    private String offerName;
    private float offerPrice;
    private String timeslot;
}
