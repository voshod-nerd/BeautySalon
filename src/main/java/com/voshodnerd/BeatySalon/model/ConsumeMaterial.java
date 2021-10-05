package com.voshodnerd.BeatySalon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ConsumeMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    Material material;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    ServiceItem serviceItem;
    Integer quantity;
}
