package com.voshodnerd.BeatySalon.jpa;

import com.voshodnerd.BeatySalon.model.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CashierRepository extends JpaRepository<Cashier, UUID> {
    Optional<Cashier> findById(UUID uuid);
}
