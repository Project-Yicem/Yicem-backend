package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.models.Admin;
import com.yicem.backend.yicem.repositories.AdminRepository;
import com.yicem.backend.yicem.repositories.UserRepository;
import com.yicem.backend.yicem.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private final AdminService adminService;

    @GetMapping("/all")
    public List<Admin> getAdmins() {
        return adminRepository.findAll();
    }

    @DeleteMapping("/delete-admin/{id}")
    public String deleteAdmin(@PathVariable String id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            userRepository.deleteById(id);
            return "Deleted admin";
        }

        return "Admin does not exist";
    }

    @PostMapping("/approve-seller/{sellerId}")
    public ResponseEntity<?> approveSellers(@RequestHeader HttpHeaders header, @PathVariable String sellerId) {
        return adminService.approveSellers(header, sellerId);
    }

    @DeleteMapping("/delete-seller/{sellerId}")
    public ResponseEntity<?> deleteSeller(@RequestHeader HttpHeaders header, @PathVariable String sellerId) {
        return adminService.deleteSeller(header, sellerId);
    }

    @DeleteMapping("/delete-buyer/{buyerId}")
    public ResponseEntity<?> deleteBuyer(@RequestHeader HttpHeaders header, @PathVariable String buyerId) {
        return adminService.deleteBuyer(header, buyerId);
    }


    @DeleteMapping("/delete-review/{reviewId}")
    public ResponseEntity<?> deleteReview(@RequestHeader HttpHeaders header, @PathVariable String reviewId) {
        return adminService.deleteReview(header, reviewId);
    }

    @GetMapping("/reports/{businessId}")
    public ResponseEntity<?> getReports(@RequestHeader HttpHeaders header, @PathVariable String businessId) {
        return adminService.getReportsOfTheBusiness(header, businessId);
    }
}