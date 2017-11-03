package com.marcosevaristo.trackussource.model;

import java.util.List;

public class Municipio {
    private Long id;
    private String nome;
    private List<Linha> lLinhas;
    private boolean ehMunicipioAtual;

    public Municipio(){}

    public Municipio(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Linha> getlLinhas() {
        return lLinhas;
    }

    public void setlLinhas(List<Linha> lLinhas) {
        this.lLinhas = lLinhas;
    }

    public boolean isEhMunicipioAtual() {
        return ehMunicipioAtual;
    }

    public void setEhMunicipioAtual(boolean ehMunicipioAtual) {
        this.ehMunicipioAtual = ehMunicipioAtual;
    }
}
