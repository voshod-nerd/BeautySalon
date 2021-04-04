package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.UserRepository;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.service.MasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master")
public class MasterController {

    private final MasterService masterService;
    private final UserRepository userRepository;

    @PutMapping("/update")
    public ResponseEntity<Users> updateMaster(@Valid @RequestBody Users user) {
        user = userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

}
