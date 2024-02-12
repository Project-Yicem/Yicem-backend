package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.dtos.BuyerDTO;
import com.yicem.backend.yicem.repositories.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public BuyerDTO save(BuyerDTO buyerDTO){
        return new BuyerDTO(buyerRepository.save(buyerDTO.toBuyer()));
    }

    public List<BuyerDTO> saveAll(List<BuyerDTO> buyers) {
        return buyers.stream()
                .map(BuyerDTO::toBuyer)
                .peek(buyerRepository::save)
                .map(BuyerDTO::new)
                .toList();
    }

    public List<BuyerDTO> findAll() {
        return buyerRepository.findAll().stream().map(BuyerDTO::new).toList();
    }

    public List<BuyerDTO> findAll(List<String> ids) {
        return buyerRepository.findAll(ids).stream().map(BuyerDTO::new).toList();
    }

    public BuyerDTO findOne(String id) {
        return new BuyerDTO(buyerRepository.findOne(id));
    }

    public long count() {
        return buyerRepository.count();
    }

    public long delete(String id) {
        return buyerRepository.delete(id);
    }

    public long delete(List<String> ids) {
        return buyerRepository.delete(ids);
    }

    public long deleteAll() {
        return buyerRepository.deleteAll();
    }

    public BuyerDTO update(BuyerDTO BuyerDTO) {
        return new BuyerDTO(buyerRepository.update(BuyerDTO.toBuyer()));
    }
    
    public long update(List<BuyerDTO> buyers) {
        return buyerRepository.update(buyers.stream().map(BuyerDTO::toBuyer).toList());
    }

}
