package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.dtos.BuyerDTO;
import com.yicem.backend.yicem.services.BuyerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BuyerController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BuyerController.class);
    private final BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @PostMapping("buyer")
    @ResponseStatus(HttpStatus.CREATED)
    public BuyerDTO postBuyer(@RequestBody BuyerDTO buyerDTO){
        return buyerService.save(buyerDTO);
    }

    @PostMapping("buyers")
    @ResponseStatus(HttpStatus.CREATED)
    public List<BuyerDTO> postBuyers(@RequestBody List<BuyerDTO> buyers) {
        return buyerService.saveAll(buyers);
    }

    @GetMapping("buyers")
    public List<BuyerDTO> getBuyers() {
        return buyerService.findAll();
    }

    @GetMapping("buyer/{id}")
    public ResponseEntity<BuyerDTO> getBuyer(@PathVariable String id) {
        BuyerDTO BuyerDTO = buyerService.findOne(id);
        if (BuyerDTO == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(BuyerDTO);
    }

    @GetMapping("buyers/{ids}")
    public List<BuyerDTO> getBuyers(@PathVariable String ids) {
        List<String> listIds = List.of(ids.split(","));
        return buyerService.findAll(listIds);
    }

    @GetMapping("buyers/count")
    public Long getCount() {
        return buyerService.count();
    }

    @DeleteMapping("buyer/{id}")
    public Long deleteBuyer(@PathVariable String id) {
        return buyerService.delete(id);
    }

    @DeleteMapping("buyers/{ids}")
    public Long deleteBuyers(@PathVariable String ids) {
        List<String> listIds = List.of(ids.split(","));
        return buyerService.delete(listIds);
    }

    @DeleteMapping("buyers")
    public Long deleteBuyers() {
        return buyerService.deleteAll();
    }

    @PutMapping("buyer")
    public BuyerDTO putBuyer(@RequestBody BuyerDTO BuyerDTO) {
        return buyerService.update(BuyerDTO);
    }

    @PutMapping("buyers")
    public Long putBuyer(@RequestBody List<BuyerDTO> buyers) {
        return buyerService.update(buyers);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return e;
    }
}
