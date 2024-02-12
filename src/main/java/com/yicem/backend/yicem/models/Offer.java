package com.yicem.backend.yicem.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class Offer {

   private ObjectId id;
   private String description;

   private boolean isMysteryBox;

   private float price;

   private int itemCount;

   private String offerName;

   private List<String> categories;

   private boolean isReserved;

   private boolean isCompleted;

   //private List<Reservation> reservations;

   private Date offeredAt;

}
