package com.example.camara_itajai.models;

import com.google.gson.annotations.SerializedName;

public class AgendamentoExistenteResponse {
    @SerializedName("exists")
    private boolean exists;

    @SerializedName("message")
    private String message;

    // Getters e Setters

    public boolean isExists() {
        return exists;
    }

    public String getMessage() {
        return message;
    }
}