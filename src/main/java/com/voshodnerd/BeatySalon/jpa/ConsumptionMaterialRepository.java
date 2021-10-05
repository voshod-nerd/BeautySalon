package com.voshodnerd.BeatySalon.jpa;

import com.voshodnerd.BeatySalon.model.ConsumeMaterial;
import com.voshodnerd.BeatySalon.model.Discount;
import com.voshodnerd.BeatySalon.model.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConsumptionMaterialRepository extends JpaRepository<ConsumeMaterial, Long> {
    @Override
    @Query(nativeQuery = true, value = "delete from consume_material where id=?1")
    void deleteById(Long id);

    Optional<List<ConsumeMaterial>> findByServiceItem(ServiceItem serviceItem);
}
