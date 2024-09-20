package com.example.camara_itajai.models;

import java.util.List;

public class HorariosResponse {
    private String message;
    private List<HorarioDisponivel> horarios;
    private String servico_nome;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<HorarioDisponivel> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioDisponivel> horarios) {
        this.horarios = horarios;
    }

    public String getServicoNome() {
        return servico_nome;
    }

    public void setServicoNome(String servico_nome) {
        this.servico_nome = servico_nome;
    }
}
