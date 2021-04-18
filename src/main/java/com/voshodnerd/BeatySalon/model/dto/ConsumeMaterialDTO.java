package com.voshodnerd.BeatySalon.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsumeMaterialDTO {
    Long idMaterial;
    String name;
    Integer quantity;
}
