package com.marcosevaristo.trackussource.model;

import java.util.List;

public class Cidade {
    private String id;
    private String nome;
    private Estado estado;
    private List<Linha> lLinhas;

    public Cidade(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Linha> getlLinhas() {
        return lLinhas;
    }

    public void setlLinhas(List<Linha> lLinhas) {
        this.lLinhas = lLinhas;
    }
}
