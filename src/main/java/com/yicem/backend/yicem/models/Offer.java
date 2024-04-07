package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Offer {
    @Id
    @NonNull
    private String id;

    @NonNull
    private String description;

    @NonNull
    private boolean isMysteryBox;

    @NonNull
    private float price;

    @NonNull
    private int itemCount;

    @NonNull
    private String offerName;


    private List<String> categories;

    @NonNull
    private boolean isReserved;

    @NonNull
    private boolean isCompleted;

    @NonNull
    private Date offeredAt;

    @NonNull
    @DBRef
    private List<Reservation> reservations;

}
