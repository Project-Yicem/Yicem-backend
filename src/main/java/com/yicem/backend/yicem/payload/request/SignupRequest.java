package com.yicem.backend.yicem.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    // Common attributes
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    private Set<String> roles;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    //Seller attributes
    private boolean isApproved;
    @Size(max = 100)
    private String address;
    @Size(max = 10)
    private String phone;
    @Size(max = 20)
    private String businessName;
    private String openingHour;
    private String closingHour;
    private String locationCoordinates;
    private float reservationTimeout;

}
