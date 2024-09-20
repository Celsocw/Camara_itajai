package com.example.camara_itajai.models;

import com.google.gson.annotations.SerializedName;

public class Agendamento {
    private final int id;
    private final String data;
    private final String hora;

    @SerializedName("servico_nome")
    private final String servicoNome;

    // Construtor
    public Agendamento(int id, String data, String hora, String servicoNome) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.servicoNome = servicoNome;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    public String getServicoNome() {
        return servicoNome;
    }

    // Sobrescrevendo o método toString
    @Override
    public String toString() {
        return "Data: " + data + ", Horário: " + hora + ", Serviço: " + servicoNome;
    }
}