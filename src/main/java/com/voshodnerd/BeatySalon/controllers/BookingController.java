package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createNewBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        bookingDTO = bookingService.createBooking(bookingDTO);
        return new ResponseEntity(new ApiResponse(true, "Booking created Successfully", bookingDTO), HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        bookingDTO = bookingService.updateBooking(bookingDTO);
        return new ResponseEntity(new ApiResponse(true, "Booking updated Successfully", bookingDTO), HttpStatus.OK);
    }

}
