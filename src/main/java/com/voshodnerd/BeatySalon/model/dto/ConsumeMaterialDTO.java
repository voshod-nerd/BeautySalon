package com.voshodnerd.BeatySalon.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsumeMaterialDTO {
    Long id;
    Long idMaterial;
    Long serviceId;
    String name;
    Integer quantity;
}
