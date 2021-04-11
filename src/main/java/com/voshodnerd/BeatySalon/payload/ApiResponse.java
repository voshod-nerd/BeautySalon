package com.voshodnerd.BeatySalon.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private Boolean success;
    private String message;
    private Object body;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public ApiResponse(Boolean success, String message,Object body) {
        this.success = success;
        this.message = message;
        this.body=body;
    }


}