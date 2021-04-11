package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.BookingRepository;
import com.voshodnerd.BeatySalon.model.Booking;
import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import com.voshodnerd.BeatySalon.payload.ApiResponse;
import com.voshodnerd.BeatySalon.service.BookingService;
import com.voshodnerd.BeatySalon.utils.MessageConstant;
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
    private final BookingRepository bookingRepository;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createNewBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        ApiResponse response = bookingService.bookingValidation(bookingDTO);
        if (response.getSuccess()) {
            Booking booking = bookingRepository.save((Booking) response.getBody());
            response.setBody(booking);
            BookingDTO bkg = bookingService.toBookingDTO((Booking) response.getBody());
            response.setBody(bkg);
            response.setMessage(MessageConstant.BOOKING_IS_SUCCESSFUL);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(true, "Booking created Successfully", bookingDTO), HttpStatus.OK);
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> validateNewBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        ApiResponse response = bookingService.bookingValidation(bookingDTO);
        if (response.getSuccess()) {
            BookingDTO bkg = bookingService.toBookingDTO((Booking) response.getBody());
            response.setBody(bkg);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateBooking(@Valid @RequestBody BookingDTO bookingDTO) {
        bookingDTO = bookingService.updateBooking(bookingDTO);
        return new ResponseEntity(new ApiResponse(true, "Booking updated Successfully", bookingDTO), HttpStatus.OK);
    }

}
