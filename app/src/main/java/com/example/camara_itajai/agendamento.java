package com.example.camara_itajai;

public class agendamento {
    private int id;
    private String data;
    private String horario;
    private String tipoServico;
    private String servicoNome;

    // Getters e setters
    public String getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }
    @Override
    public String toString() {
        return "Data: " + data + ", Horário: " + horario + ", Serviço: " + servicoNome;
    }
}