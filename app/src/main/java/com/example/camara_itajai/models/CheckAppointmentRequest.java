package com.example.camara_itajai.models;

public class CheckAppointmentRequest {
    private String email;
    private int setor_id;

    public CheckAppointmentRequest(String email, int setorId) {
        this.email = email;
        this.setor_id = setor_id;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public int getSetorId() {
        return setor_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSetorId(int setor_id) {
        this.setor_id = setor_id;
    }
}