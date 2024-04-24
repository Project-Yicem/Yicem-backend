package com.yicem.backend.yicem.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    @Size(max = 200)
    private String comment;
    @Min(value = 1)
    @Max(value = 5)
    private int rating;
}
