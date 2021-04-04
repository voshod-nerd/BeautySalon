package com.voshodnerd.BeatySalon.model.authentication;

public enum  RoleName {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_MASTER;

    public static RoleName getEnumByName(String value) {
        if (value.equals("ROLE_USER")) return RoleName.ROLE_USER;
        if (value.equals("ROLE_ADMIN")) return RoleName.ROLE_ADMIN;
        if (value.equals("ROLE_MASTER")) return RoleName.ROLE_MASTER;
        return  RoleName.ROLE_USER;
    }
}
