package com.yicem.backend.yicem.dtos;

import com.yicem.backend.yicem.models.Seller;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public record SellerDTO(
        String id,
        String username,
        String email,
        String password,
        boolean isApproved,
        String address,
        String phone,
        String businessName,
        String workingHours,
        String locationCoordinates,
        float reservationTimeout,
        List<String> reviews,
        List<String> currentOffers,
        List<String> pastTransactions) {

    public SellerDTO(Seller s) {
        this(s.getId() == null ? ObjectId.get().toHexString() : s.getId().toHexString(), s.getUsername(),
                s.getEmail(), s.getPassword(), s.isApproved(), s.getAddress(), s.getPhone(), s.getBusinessName(),
                s.getWorkingHours(), s.getLocationCoordinates(), s.getReservationTimeout(),
                s.getReviews().stream().map(ObjectId::toHexString).collect(Collectors.toList()),
                s.getCurrentOffers().stream().map(ObjectId::toHexString).collect(Collectors.toList()),
                s.getPastTransactions().stream().map(ObjectId::toHexString).collect(Collectors.toList()));
    }

    public Seller toSeller() {
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return Seller.builder()
                .id(_id)
                .username(username)
                .email(email)
                .password(password)
                .isApproved(isApproved)
                .address(address)
                .phone(phone)
                .businessName(businessName)
                .workingHours(workingHours)
                .locationCoordinates(locationCoordinates)
                .reservationTimeout(reservationTimeout)
                .reviews(reviews.stream().map(ObjectId::new).collect(Collectors.toList()))
                .currentOffers(currentOffers.stream().map(ObjectId::new).collect(Collectors.toList()))
                .pastTransactions(pastTransactions.stream().map(ObjectId::new).collect(Collectors.toList()))
                .build();
    }

}
