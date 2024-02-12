package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.dtos.OfferDTO;
import com.yicem.backend.yicem.services.OfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OfferController {

    private final static Logger LOGGER = LoggerFactory.getLogger(OfferController.class);
    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("offer")
    @ResponseStatus(HttpStatus.CREATED)
    public OfferDTO postOffer(@RequestBody OfferDTO offerDTO){
        return offerService.save(offerDTO);
    }

    @PostMapping("offers")
    @ResponseStatus(HttpStatus.CREATED)
    public List<OfferDTO> postOffers(@RequestBody List<OfferDTO> offers) {
        return offerService.saveAll(offers);
    }

    @GetMapping("offers")
    public List<OfferDTO> getOffers() {
        return offerService.findAll();
    }

    @GetMapping("offer/{id}")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable String id) {
        OfferDTO OfferDTO = offerService.findOne(id);
        if (OfferDTO == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(OfferDTO);
    }

    @GetMapping("offers/{ids}")
    public List<OfferDTO> getOffers(@PathVariable String ids) {
        List<String> listIds = List.of(ids.split(","));
        return offerService.findAll(listIds);
    }

    @GetMapping("offers/count")
    public Long getCount() {
        return offerService.count();
    }

    @DeleteMapping("offer/{id}")
    public Long deleteOffer(@PathVariable String id) {
        return offerService.delete(id);
    }

    @DeleteMapping("offers/{ids}")
    public Long deleteOffers(@PathVariable String ids) {
        List<String> listIds = List.of(ids.split(","));
        return offerService.delete(listIds);
    }

    @DeleteMapping("offers")
    public Long deleteOffers() {
        return offerService.deleteAll();
    }

    @PutMapping("offer")
    public OfferDTO putOffer(@RequestBody OfferDTO OfferDTO) {
        return offerService.update(OfferDTO);
    }

    @PutMapping("offers")
    public Long putOffer(@RequestBody List<OfferDTO> offers) {
        return offerService.update(offers);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return e;
    }
}

