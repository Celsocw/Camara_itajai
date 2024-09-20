package com.example.camara_itajai;

import com.example.camara_itajai.models.Agendamento;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AgendamentoResponse {
    @SerializedName("agendamentos")
    private List<Agendamento> agendamentos;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    // Getter e Setter

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}