package com.voshodnerd.BeatySalon.payload;

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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}