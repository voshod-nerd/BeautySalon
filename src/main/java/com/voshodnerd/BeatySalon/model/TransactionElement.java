package com.voshodnerd.BeatySalon.model;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class TransactionElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    OperationType operation;
    Float sum;
    Date time;
    String description;
}
