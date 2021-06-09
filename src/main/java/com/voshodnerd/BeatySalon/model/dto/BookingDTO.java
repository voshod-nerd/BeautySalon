package com.voshodnerd.BeatySalon.model.dto;

import com.voshodnerd.BeatySalon.model.ServiceItem;
import com.voshodnerd.BeatySalon.model.StatusBooking;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.joda.time.DateTime;

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
    Set<ServiceItem> serviceList = new HashSet<>();
    @NotNull
    Date dateB;
    Date dateE;
    Float sum;
    Float totalSum;
    String promoCode;
    Integer rate;
    StatusBooking statusBooking;
}
