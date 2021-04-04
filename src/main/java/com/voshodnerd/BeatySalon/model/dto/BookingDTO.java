package com.voshodnerd.BeatySalon.model.dto;

import com.voshodnerd.BeatySalon.model.ServiceItem;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.*;

@Getter
@Setter
public class BookingDTO {
    @Id
    Long id;
    @NotNull
    MasterDTO master;
    @NotNull
    UserDTO user;
    @NotNull
    Set<ServiceItem> serviceList;
    Date date;
    Long sum;
    Long totalSum;
}
