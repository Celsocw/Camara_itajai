package com.example.camara_itajai.models;

public class Servico {
    private final int id;
    private final String nome;
    private final int capacidade;
    private int setorId;
    private String setorNome;

    // Construtor existente
    public Servico(int id, String nome, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
    }

    // Adicione o novo construtor que inclui setorId (se necessário)
    public Servico(int id, String nome, int capacidade, int setorId) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.setorId = setorId;
    }

    // Getters existentes
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    // Novo getter para setorId
    public int getSetorId() {
        return setorId;
    }

    // Método toString
    @Override
    public String toString() {
        return nome;
    }
}