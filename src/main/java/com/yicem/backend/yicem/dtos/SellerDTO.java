package com.yicem.backend.yicem.dtos;

import com.yicem.backend.yicem.models.Seller;
import org.bson.types.ObjectId;

public record SellerDTO(
        String id,
        String username,
        String email,
        String password,
        String address,
        String phone,
        boolean isApproved,
        String businessName,
        float reservationTimeout,
        String workingHours,
        String locationCoordinates) {

    public SellerDTO(Seller s) {
        this(s.getId() == null ? ObjectId.get().toHexString() : s.getId().toHexString(), s.getUsername(),
                s.getEmail(), s.getPassword(), s.getAddress(), s.getPhone(), s.isApproved(),
                s.getBusinessName(), s.getReservationTimeout(), s.getWorkingHours(), s.getLocationCoordinates());
    }

    public Seller toSeller() {
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return Seller.builder()
                .id(_id)
                .username(username)
                .email(email)
                .password(password)
                .address(address)
                .phone(phone)
                .isApproved(isApproved)
                .businessName(businessName)
                .reservationTimeout(reservationTimeout)
                .workingHours(workingHours)
                .locationCoordinates(locationCoordinates)
                .build();
    }

}
