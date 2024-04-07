package com.yicem.backend.yicem.controllers;

import com.yicem.backend.yicem.models.Admin;
import com.yicem.backend.yicem.repositories.AdminRepository;
import com.yicem.backend.yicem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/all")
    public List<Admin> getAdmins() {
        return adminRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable String id){
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            userRepository.deleteById(id);
            return "Deleted admin";
        }

        return "Admin does not exist";
    }



}