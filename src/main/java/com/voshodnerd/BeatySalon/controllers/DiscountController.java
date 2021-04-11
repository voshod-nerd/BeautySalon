package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.DiscountRepository;
import com.voshodnerd.BeatySalon.model.Discount;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountRepository repository;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNewDiscount(@Valid @RequestBody Discount discount) {
        discount = repository.save(discount);
        return new ResponseEntity(new ApiResponse(true, "Discount created Successfully", discount), HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateDiscount(@Valid @RequestBody Discount discount) {
        discount = repository.save(discount);
        return new ResponseEntity(new ApiResponse(true, "Discount updated Successfully", discount), HttpStatus.OK);
    }
}
