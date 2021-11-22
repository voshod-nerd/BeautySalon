package com.voshodnerd.BeatySalon.model;

import com.voshodnerd.BeatySalon.model.authentication.Users;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    Users users;
    Date dateB;
    Date dateE;
    BigDecimal sum;
    BigDecimal totalSum;
    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    Users master;
    @ManyToMany
    Set<ServiceItem> serviceList;
    Integer rate;
    @Enumerated(EnumType.STRING)
    StatusBooking status;
}
