package com.voshodnerd.BeatySalon.jpa;

import com.voshodnerd.BeatySalon.model.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceItemRepository  extends JpaRepository<ServiceItem,Long> {
}
