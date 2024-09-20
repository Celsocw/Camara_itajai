package com.example.camara_itajai.models;

import com.google.gson.annotations.SerializedName;

public class UserRegistrationResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("userId")
    private int userId;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }
}

