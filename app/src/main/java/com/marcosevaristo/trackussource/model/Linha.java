package com.marcosevaristo.trackussource.model;

import java.io.Serializable;

public class Linha implements Serializable {
    private Long idSql;
    private String id;
    private Municipio municipio;
    private String numero;
    private String titulo;
    private String subtitulo;
    private boolean ehLinhaAtual = false;
    private boolean selecionada = false;

    private static final long serialVersionUID = 1L;

    public Linha() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public Long getIdSql() {
        return idSql;
    }

    public void setIdSql(Long idSql) {
        this.idSql = idSql;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.numero);
        sb.append(" - ");
        sb.append(this.titulo);
        if(this.subtitulo != null) {
            sb.append(" - ");
            sb.append(this.subtitulo);
        }
        return sb.toString();
    }

    public String toStringMainTextOnly() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.numero);
        sb.append(" - ");
        sb.append(this.titulo);
        return sb.toString();
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public boolean ehLinhaAtual() {
        return ehLinhaAtual;
    }

    public void setEhLinhaAtual(boolean ehLinhaAtual) {
        this.ehLinhaAtual = ehLinhaAtual;
    }

    public boolean isSelecionada() {
        return selecionada;
    }

    public void setSelecionada(boolean selecionada) {
        this.selecionada = selecionada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
