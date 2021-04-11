package com.voshodnerd.BeatySalon.jpa;

import com.voshodnerd.BeatySalon.model.Booking;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookingRepository  extends JpaRepository<Booking,Long> {
    Optional<List<Booking>> findByMaster(Users master);
    Optional<List<Booking>> findByDateBetweenAndMaster(DateTime start, DateTime end, Users master);
}
