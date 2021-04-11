package com.voshodnerd.BeatySalon.jpa;

import com.voshodnerd.BeatySalon.model.Discount;
import com.voshodnerd.BeatySalon.model.TypeDiscount;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByType(TypeDiscount typeDiscount);

    @Query(value = "select d from Discount d where (d.startDate<=?1 and d.endDate>=?1) and d.users=?2 ")
    Optional<List<Discount>> findByPeriodAndUser(Date dateTime, Long user);
}
