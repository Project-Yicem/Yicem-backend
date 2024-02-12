package com.yicem.backend.yicem.dtos;

import com.yicem.backend.yicem.models.Offer;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public record OfferDTO(
        String id,
        String description,
        boolean isMysterybox,
        float price,
        int itemCount,
        String offerName,
        List<String> categories,
        boolean isReserved,
        boolean isCompleted,
        Date offeredAt) {

    public OfferDTO(Offer o) {
        this(o.getId() == null ? ObjectId.get().toHexString() : o.getId().toHexString(), o.getDescription(),
                o.isMysteryBox(), o.getPrice(), o.getItemCount(), o.getOfferName(), o.getCategories(),
                o.isReserved(), o.isCompleted(), o.getOfferedAt());
    }

    public Offer toOffer() {
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return Offer.builder()
                .id(_id)
                .description(description)
                .isMysteryBox(isMysterybox)
                .price(price)
                .itemCount(itemCount)
                .offerName(offerName)
                .categories(categories)
                .isReserved(isReserved)
                .isCompleted(isCompleted)
                .offeredAt(offeredAt)
                .build();
    }

}
