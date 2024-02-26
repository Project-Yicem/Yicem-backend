package com.yicem.backend.yicem.models;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
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
   private Date offeredAt;
   private List<ObjectId> reservations;

   @Builder
   public Offer(ObjectId id, String description, boolean isMysteryBox, float price, int itemCount, String offerName,
                List<String> categories, boolean isReserved, boolean isCompleted, Date offeredAt,
                List<ObjectId> reservations) {
      this.id = id;
      this.description = description;
      this.isMysteryBox = isMysteryBox;
      this.price = price;
      this.itemCount = itemCount;
      this.offerName = offerName;
      this.categories = categories;
      this.isReserved = isReserved;
      this.isCompleted = isCompleted;
      this.offeredAt = offeredAt;
      this.reservations = reservations;
   }
}
