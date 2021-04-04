package com.voshodnerd.BeatySalon.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Date;

public class Discount {
    @Id
    Long id;
    Date startDate;
    Date endDate;
    @Enumerated(EnumType.STRING)
    TypeDiscount type;
    Long value;
}
