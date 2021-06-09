package com.voshodnerd.BeatySalon.model.dto;

import com.voshodnerd.BeatySalon.model.TypeDiscount;
import com.voshodnerd.BeatySalon.model.authentication.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {
    Long id;
    Date startDate;
    Date endDate;
    String name;
    Long users;
    TypeDiscount type;
    String description;
    Integer value;
}
