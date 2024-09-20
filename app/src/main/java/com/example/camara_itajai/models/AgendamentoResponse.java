package com.example.camara_itajai.models;

import java.util.List;

public class AgendamentoResponse {
    private final boolean success;
    private final String message;
    private final List<Agendamento> agendamentos; // Adicione este atributo

    // Construtor atualizado
    public AgendamentoResponse(boolean success, String message, List<Agendamento> agendamentos) {
        this.success = success;
        this.message = message;
        this.agendamentos = agendamentos; // Inicialize o atributo
    }

    // Getter para success
    public boolean isSuccess() {
        return success;
    }

    // Getter para message
    public String getMessage() {
        return message;
    }

    // Getter para agendamentos
    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }
}