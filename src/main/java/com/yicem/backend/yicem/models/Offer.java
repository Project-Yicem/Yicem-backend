package com.yicem.backend.yicem.models;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

public class Offer {

   @Id
   private String id;
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
