package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.model.ServiceItem;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.service.ManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service")
@Tag(name = "Контроллер услуг салона", description = "Методы по работе с услугами")
public class ServiceController {
    private final ManageService manageService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Создание услуги салона",
            description = ""
    )
    public ResponseEntity<?> createService(@Valid @RequestBody ServiceItem service) {
        service = manageService.createService(service);
        return new ResponseEntity(new ApiResponse(true, "Service created Successfully", service), HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновление услуги салона",
            description = ""
    )
    public ResponseEntity<?> updateService(@Valid @RequestBody ServiceItem service) {
        service = manageService.updateService(service);


        return new ResponseEntity(new ApiResponse(true, "Service updated Successfully", service), HttpStatus.OK);
    }


}
