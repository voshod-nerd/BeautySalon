package com.voshodnerd.BeatySalon.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class TransactionElement {
    @Id
    Long id;
    @Enumerated(EnumType.STRING)
    OperationType operation;
    Long sum;
    Date time;
    String description;
}
