package com.voshodnerd.BeatySalon.jpa;

import com.voshodnerd.BeatySalon.model.TransactionElement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionElement,Long> {
}
