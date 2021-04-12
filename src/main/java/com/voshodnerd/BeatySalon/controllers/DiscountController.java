package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.DiscountRepository;
import com.voshodnerd.BeatySalon.model.Discount;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Контроллер скидок", description = "Методы по работе со скидками")
public class DiscountController {

    private final DiscountRepository repository;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Создание новой скидки",
            description = "Создание новой скидки"
    )
    public ResponseEntity<?> createNewDiscount(@Valid @RequestBody Discount discount) {
        discount = repository.save(discount);
        return new ResponseEntity(new ApiResponse(true, "Discount created Successfully", discount), HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновление скидки",
            description = "Обновление скидки"
    )
    public ResponseEntity<?> updateDiscount(@Valid @RequestBody Discount discount) {
        discount = repository.save(discount);
        return new ResponseEntity(new ApiResponse(true, "Discount updated Successfully", discount), HttpStatus.OK);
    }
}
