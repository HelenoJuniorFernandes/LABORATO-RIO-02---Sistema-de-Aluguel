package com.pucminas.rental_system.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Rendimento {
    private String entidadeEmpregadora;
    private Double valor;

    // Getters and Setters
    public String getEntidadeEmpregadora() { return entidadeEmpregadora; }
    public void setEntidadeEmpregadora(String entidadeEmpregadora) { this.entidadeEmpregadora = entidadeEmpregadora; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
}
