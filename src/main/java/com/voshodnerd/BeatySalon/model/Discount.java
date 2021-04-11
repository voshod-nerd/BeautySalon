package com.voshodnerd.BeatySalon.model;

import com.voshodnerd.BeatySalon.model.authentication.Users;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Date startDate;
    Date endDate;
    String name;
    @ManyToOne
    @JoinColumn(name = "master_id", nullable = true)
    Users users;
    @Enumerated(EnumType.STRING)
    TypeDiscount type;
    Integer value;
}
