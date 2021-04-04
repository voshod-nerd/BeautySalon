package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.UserRepository;
import com.voshodnerd.BeatySalon.model.ServiceItem;
import com.voshodnerd.BeatySalon.model.authentication.RoleName;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.model.dto.BookingDTO;
import com.voshodnerd.BeatySalon.service.BookingService;
import com.voshodnerd.BeatySalon.service.ManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final UserRepository userRepository;
    private final ManageService manageService;
    private final BookingService bookingService;

    @GetMapping("/master/all")
    public List<Users> getAllMaster() {
        List<Users> result = userRepository.findAll().stream()
                .filter(x -> x.getRole().equals(RoleName.ROLE_MASTER)).
                        filter(x -> x.getActive())
                .collect(Collectors.toList());
        return result;
    }

    @PostMapping("/booking_list/{masterId}")
    public ResponseEntity<?> listBookingByMaster(@PathVariable Long masterId) {
        List<BookingDTO> lst = bookingService.getBookingByMasterId(masterId);
        return new ResponseEntity<List<BookingDTO>>(lst, HttpStatus.OK);
    }

    @GetMapping("/service/all")
    public List<ServiceItem> getAllServiceList() {
        return manageService.getAllActiveService();
    }
}
