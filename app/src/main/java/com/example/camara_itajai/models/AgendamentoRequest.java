package com.example.camara_itajai.models;

public class AgendamentoRequest {
    private String email;
    private int setor_id;
    private int servicoId;
    private long data; // Timestamp ou outra representação de data
    private int horarioId;
    private int userId;

    // Construtor
    public AgendamentoRequest(String email, int setor_id, int servicoId, long data, int horarioId, int userId) {
        this.email = email;
        this.setor_id = setor_id;
        this.servicoId = servicoId;
        this.data = data;
        this.horarioId = horarioId;
        this.userId = userId;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSetorId() {
        return setor_id;
    }

    public void setSetorId(int setor_id) {
        this.setor_id = setor_id;
    }

    public int getServicoId() {
        return servicoId;
    }

    public void setServicoId(int servicoId) {
        this.servicoId = servicoId;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public int getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(int horarioId) {
        this.horarioId = horarioId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}