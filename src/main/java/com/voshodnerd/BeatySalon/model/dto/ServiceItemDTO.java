package com.voshodnerd.BeatySalon.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voshodnerd.BeatySalon.model.ConsumeMaterial;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ServiceItemDTO {
    Long id;
    String name;
    Integer price;
    Integer durationInMinute;
    String category;
    Boolean active;
    String description;
    Set<ConsumeMaterialDTO> materialList;
}
