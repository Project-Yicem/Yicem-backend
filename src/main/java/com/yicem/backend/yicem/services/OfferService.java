package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.dtos.OfferDTO;
import com.yicem.backend.yicem.repositories.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {
    private final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public OfferDTO save(OfferDTO offerDTO){
        return new OfferDTO(offerRepository.save(offerDTO.toOffer()));
    }

    public List<OfferDTO> saveAll(List<OfferDTO> offers) {
        return offers.stream()
                .map(OfferDTO::toOffer)
                .peek(offerRepository::save)
                .map(OfferDTO::new)
                .toList();
    }

    public List<OfferDTO> findAll() {
        return offerRepository.findAll().stream().map(OfferDTO::new).toList();
    }

    public List<OfferDTO> findAll(List<String> ids) {
        return offerRepository.findAll(ids).stream().map(OfferDTO::new).toList();
    }

    public OfferDTO findOne(String id) {
        return new OfferDTO(offerRepository.findOne(id));
    }

    public long count() {
        return offerRepository.count();
    }

    public long delete(String id) {
        return offerRepository.delete(id);
    }

    public long delete(List<String> ids) {
        return offerRepository.delete(ids);
    }

    public long deleteAll() {
        return offerRepository.deleteAll();
    }

    public OfferDTO update(OfferDTO OfferDTO) {
        return new OfferDTO(offerRepository.update(OfferDTO.toOffer()));
    }

    public long update(List<OfferDTO> offers) {
        return offerRepository.update(offers.stream().map(OfferDTO::toOffer).toList());
    }

}

