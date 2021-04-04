package com.voshodnerd.BeatySalon.model.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voshodnerd.BeatySalon.model.ServiceItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 15)
    @Column(unique=true)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    @Column(unique=true)
    private String email;

    @NotBlank
    @Size(max = 100)
    @JsonIgnore
    private String password;
    private Boolean active;
    @Enumerated(EnumType.STRING)
    private RoleName role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "skills",
            joinColumns = @JoinColumn(name = "master_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<ServiceItem> skills = new HashSet<>();

    public Users(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
