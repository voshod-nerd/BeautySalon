package com.voshodnerd.BeatySalon.model;

import com.voshodnerd.BeatySalon.model.authentication.Users;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    Users users;
    Date date;
    Long sum;
    Long totalSum;
    @ManyToOne
    Users master;
    @ManyToMany
    Set<ServiceItem> serviceList;
    @Enumerated(EnumType.STRING)
    StatusBooking status;
}
