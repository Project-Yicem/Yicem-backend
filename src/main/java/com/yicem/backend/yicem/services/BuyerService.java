package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.dtos.BuyerDTO;
import com.yicem.backend.yicem.repositories.BuyerRepository;
import org.springframework.stereotype.Service;

@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public BuyerDTO save(BuyerDTO buyerDTO){
        return new BuyerDTO(buyerRepository.save(buyerDTO.toBuyer()));
    }
}
