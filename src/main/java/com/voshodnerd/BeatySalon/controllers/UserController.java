package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.UserRepository;
import com.voshodnerd.BeatySalon.model.authentication.RoleName;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.payload.UserIdentityAvailability;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("api/public/users")
    public List<Users> getAllUserList() {
        List<Users> result = userRepository.findAll().stream()
                .filter(x -> x.getRole().equals(RoleName.ROLE_USER)).
                        filter(x -> x.getActive())
                .collect(Collectors.toList());
        return result;
    }

    @PutMapping("/api/user/update")
    public ResponseEntity<Users> updateUser(@Valid @RequestBody Users user) {
        user = userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/api/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

}
