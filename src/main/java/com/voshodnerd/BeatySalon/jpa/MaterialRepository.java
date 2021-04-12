package com.voshodnerd.BeatySalon.jpa;

import com.voshodnerd.BeatySalon.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material,Long> {
    Optional<Material> findByName(String name);
}
