package com.voshodnerd.BeatySalon.controllers;

import com.voshodnerd.BeatySalon.jpa.UserRepository;
import com.voshodnerd.BeatySalon.model.authentication.RoleName;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import com.voshodnerd.BeatySalon.payload.UserIdentityAvailability;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер пользователей", description = "Методы по работе с пользователями")
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> getAllUserList() {
        List<Users> result = userRepository.findAll().stream()
                        .filter(x -> x.getActive())
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/byUserId/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> getByUserId(@PathVariable(name = "userId") Long userId ) {
        List<Users> result = userRepository.findById(userId).stream()
                .filter(x -> x.getRole().equals(RoleName.ROLE_USER)).
                        filter(x -> x.getActive())
                .collect(Collectors.toList());
        return result;
    }


    @PutMapping("/update")
    public ResponseEntity<Users> updateUser(@Valid @RequestBody Users user) {
        user = userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/api/user/checkUsernameAvailability")
    @Operation(
            summary = "Проверка доступности имени пользователя",
            description = "Проверяет зарегестрирован ли такой имя пользователя. Не требует авторизации"
    )
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    @Operation(
            summary = "Проверка доступности email",
            description = "Проверяет зарегестрирован ли такой email. Не требует авторизации"
    )
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

}
