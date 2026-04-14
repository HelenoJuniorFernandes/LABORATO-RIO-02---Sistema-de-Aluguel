package com.pucminas.rental_system.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Cliente extends User {
    private String nome;
    private String rg;
    private String cpf;
    private String endereco;
    private String profissao;

    @ElementCollection
    @CollectionTable(name = "rendimentos", joinColumns = @JoinColumn(name = "cliente_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "entidadeEmpregadora", column = @Column(name = "entidade_empregadora")),
        @AttributeOverride(name = "valor", column = @Column(name = "valor"))
    })
    private List<Rendimento> rendimentos; // Máximo 3

    // Getters and Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
    public List<Rendimento> getRendimentos() { return rendimentos; }
    public void setRendimentos(List<Rendimento> rendimentos) { this.rendimentos = rendimentos; }
}
