package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.dtos.SellerDTO;
import com.yicem.backend.yicem.services.SellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SellerController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SellerController.class);
    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping("seller")
    @ResponseStatus(HttpStatus.CREATED)
    public SellerDTO postSeller(@RequestBody SellerDTO sellerDTO){
        return sellerService.save(sellerDTO);
    }

    @PostMapping("sellers")
    @ResponseStatus(HttpStatus.CREATED)
    public List<SellerDTO> postSellers(@RequestBody List<SellerDTO> sellers) {
        return sellerService.saveAll(sellers);
    }

    @GetMapping("sellers")
    public List<SellerDTO> getSellers() {
        return sellerService.findAll();
    }

    @GetMapping("seller/{id}")
    public ResponseEntity<SellerDTO> getSeller(@PathVariable String id) {
        SellerDTO SellerDTO = sellerService.findOne(id);
        if (SellerDTO == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(SellerDTO);
    }

    @GetMapping("sellers/{ids}")
    public List<SellerDTO> getSellers(@PathVariable String ids) {
        List<String> listIds = List.of(ids.split(","));
        return sellerService.findAll(listIds);
    }

    @GetMapping("sellers/count")
    public Long getCount() {
        return sellerService.count();
    }

    @DeleteMapping("seller/{id}")
    public Long deleteSeller(@PathVariable String id) {
        return sellerService.delete(id);
    }

    @DeleteMapping("sellers/{ids}")
    public Long deleteSellers(@PathVariable String ids) {
        List<String> listIds = List.of(ids.split(","));
        return sellerService.delete(listIds);
    }

    @DeleteMapping("sellers")
    public Long deleteSellers() {
        return sellerService.deleteAll();
    }

    @PutMapping("seller")
    public SellerDTO putSeller(@RequestBody SellerDTO SellerDTO) {
        return sellerService.update(SellerDTO);
    }

    @PutMapping("sellers")
    public Long putSeller(@RequestBody List<SellerDTO> sellers) {
        return sellerService.update(sellers);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return e;
    }
}
