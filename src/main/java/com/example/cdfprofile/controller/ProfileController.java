package com.example.cdfprofile.controller;

import com.example.cdfprofile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
//@CrossOrigin("*")
@RequestMapping("/")
public class ProfileController {
    @Autowired
    ProfileService profileService;
    @GetMapping("/count")
    public ResponseEntity<?> getCount() throws IOException {

        return ResponseEntity.ok().body(profileService.getCountProfile());
    }

}
