package com.voshodnerd.BeatySalon.jpa;

import com.voshodnerd.BeatySalon.model.Discount;
import com.voshodnerd.BeatySalon.model.TypeDiscount;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByType(TypeDiscount typeDiscount);

    @Query(value = "select d from Discount d where (d.startDate<=:curDate1 and d.endDate>=:curDate2) and d.users=:usr ")
    Optional<List<Discount>> findByPeriodAndUser(@Param("curDate1") Date curdate,@Param("curDate2") Date curdate1,@Param("usr") Users user);
}
