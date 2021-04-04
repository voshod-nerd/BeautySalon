package com.voshodnerd.BeatySalon.service;

import com.voshodnerd.BeatySalon.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MasterService {
    private final UserRepository userRepository;
}
