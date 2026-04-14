package com.pucminas.rental_system.model;

import jakarta.persistence.Entity;

@Entity
public class Agente extends User {
    private String nomeEmpresa;

    // Getters and Setters
    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }
}
