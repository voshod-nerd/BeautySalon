package com.voshodnerd.BeatySalon.model;

import jdk.jfr.SettingDefinition;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Cashier {
    @Id
    UUID id;
    Float totalSum;
}
