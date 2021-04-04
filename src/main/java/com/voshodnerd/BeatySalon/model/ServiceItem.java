package com.voshodnerd.BeatySalon.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
public class ServiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    Long price;
    Long durationInMinute;
    Boolean active;
    @ManyToMany
    Set<ConsumeMaterial> materialList;

}
