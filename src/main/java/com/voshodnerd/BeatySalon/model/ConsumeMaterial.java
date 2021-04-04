package com.voshodnerd.BeatySalon.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class ConsumeMaterial {
    @Id
    Long id;
    @ManyToOne
    Material materialId;
    Long quantity;
}
