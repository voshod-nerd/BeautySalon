package com.voshodnerd.BeatySalon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    Integer price;
    Integer durationInMinute;
    String category;
    Boolean active;
    @Column(columnDefinition="TEXT")
    String description;
    @OneToMany(mappedBy="serviceItem", fetch=FetchType.EAGER,cascade = CascadeType.REMOVE,orphanRemoval = true)
    @JsonIgnore
    List<ConsumeMaterial> materialList;

}
