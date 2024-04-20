package com.yicem.backend.yicem.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private String comment;
    private float rating;
}
