package com.yicem.backend.yicem.services;

import com.yicem.backend.yicem.dtos.SellerDTO;
import com.yicem.backend.yicem.repositories.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public SellerDTO save(SellerDTO sellerDTO){
        return new SellerDTO(sellerRepository.save(sellerDTO.toSeller()));
    }

    public List<SellerDTO> saveAll(List<SellerDTO> sellers) {
        return sellers.stream()
                .map(SellerDTO::toSeller)
                .peek(sellerRepository::save)
                .map(SellerDTO::new)
                .toList();
    }

    public List<SellerDTO> findAll() {
        return sellerRepository.findAll().stream().map(SellerDTO::new).toList();
    }

    public List<SellerDTO> findAll(List<String> ids) {
        return sellerRepository.findAll(ids).stream().map(SellerDTO::new).toList();
    }

    public SellerDTO findOne(String id) {
        return new SellerDTO(sellerRepository.findOne(id));
    }

    public long count() {
        return sellerRepository.count();
    }

    public long delete(String id) {
        return sellerRepository.delete(id);
    }

    public long delete(List<String> ids) {
        return sellerRepository.delete(ids);
    }

    public long deleteAll() {
        return sellerRepository.deleteAll();
    }

    public SellerDTO update(SellerDTO SellerDTO) {
        return new SellerDTO(sellerRepository.update(SellerDTO.toSeller()));
    }

    public long update(List<SellerDTO> sellers) {
        return sellerRepository.update(sellers.stream().map(SellerDTO::toSeller).toList());
    }

}
