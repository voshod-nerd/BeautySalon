package com.voshodnerd.BeatySalon.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OperationMaterial {
   @NotNull
   private Long idMaterial;
   @NotNull
   private Integer value;
}
