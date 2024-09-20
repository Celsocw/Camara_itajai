package com.example.camara_itajai.models;

import com.google.gson.annotations.SerializedName;

public class CheckAppointmentResponse {

    @SerializedName("exists")
    private boolean exists;

    @SerializedName("message")
    private String message;

    // Construtor vazio (necessário para o Gson)
    public CheckAppointmentResponse() {}

    // Getters e Setters

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}