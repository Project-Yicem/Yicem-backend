package com.yicem.backend.yicem.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
@Builder
public class Review {

    private ObjectId id;
    private ObjectId buyerId;
    private ObjectId sellerId;
    private String comment;
    private float rating;

}
