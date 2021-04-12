package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.UserRepository;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.service.MasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master")
@Tag(name = "Контроллер по работе с мастерами", description = "Методы по работе с мастерами салона")
public class MasterController {

    private final MasterService masterService;
    private final UserRepository userRepository;

    @PutMapping("/update")
    @Operation(
            summary = "Обновление мастера",
            description = "Обновление мастера"
    )
    public ResponseEntity<Users> updateMaster(@Valid @RequestBody Users user) {
        user = userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

}
