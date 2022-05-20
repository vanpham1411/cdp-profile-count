package com.example.cdfprofile;

import com.example.cdfprofile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class CdfProfileApplication {
    @Autowired
    ProfileService profileService;

    public static void main(String[] args) {
        SpringApplication.run(CdfProfileApplication.class, args);

    }
}
