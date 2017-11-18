package com.marcosevaristo.trackussource.model;

import java.util.Map;

public class Municipio {
    private Long idSql;
    private String id;
    private String nome;
    private Map<String, Linha> linhas;
    private boolean ehMunicipioAtual;
    private boolean selecionado = false;

    public Municipio(){}

    public Municipio(Long idSql) {
        this.idSql = idSql;
    }

    public Long getIdSql() {
        return idSql;
    }

    public void setIdSql(Long idSql) {
        this.idSql = idSql;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Map<String, Linha> getLinhas() {
        return linhas;
    }

    public void setLinhas(Map<String, Linha> linhas) {
        this.linhas = linhas;
    }

    public boolean isEhMunicipioAtual() {
        return ehMunicipioAtual;
    }

    public void setEhMunicipioAtual(boolean ehMunicipioAtual) {
        this.ehMunicipioAtual = ehMunicipioAtual;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.idSql).append(" - ").append(this.nome);
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
