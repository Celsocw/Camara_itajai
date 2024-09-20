package com.example.camara_itajai.models;

public class HorarioDisponivel {
    private int id;
    private String hora;
    private int vagas_disponiveis;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getVagasDisponiveis() {
        return vagas_disponiveis;
    }

    public void setVagasDisponiveis(int vagas_disponiveis) {
        this.vagas_disponiveis = vagas_disponiveis;
    }

    @Override
    public String toString() {
        return hora + " (" + vagas_disponiveis + " vaga" + (vagas_disponiveis != 1 ? "s" : "") + ")";
    }
}
