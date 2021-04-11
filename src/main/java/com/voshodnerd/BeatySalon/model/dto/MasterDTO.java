package com.voshodnerd.BeatySalon.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MasterDTO {
    @NotNull
    Long id;
    String name;
}
