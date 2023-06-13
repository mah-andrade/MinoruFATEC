package com.example.minoru.models;

public class lavagens {
    private String modelo,placa,veiculo;
    private Double ordemlavagem;

    public lavagens() {
    }

    public lavagens(String modelo, String placa,String veiculo,Double ordemlavagem) {
        this.modelo = modelo;
        this.placa = placa;
        this.veiculo = veiculo;
        this.ordemlavagem = ordemlavagem;
    }


    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public Double getOrdemlavagem() {
        return ordemlavagem;
    }
}
