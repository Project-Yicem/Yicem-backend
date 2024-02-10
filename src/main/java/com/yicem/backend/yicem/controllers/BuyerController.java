package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.dtos.BuyerDTO;
import com.yicem.backend.yicem.services.BuyerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BuyerController {
    private final BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @PostMapping("buyer")
    @ResponseStatus(HttpStatus.CREATED)
    public BuyerDTO postBuyer(@RequestBody BuyerDTO buyerDTO){
        return buyerService.save(buyerDTO);
    }
}
