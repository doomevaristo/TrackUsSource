package com.marcosevaristo.trackussource.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Linha implements Serializable {
    private Long idSql;
    private Municipio municipio;
    private String numero;
    private String titulo;
    private String subtitulo;
    private boolean ehLinhaAtual = false;
    private boolean selecionada = false;

    private static final long serialVersionUID = 1L;

    public Linha() {
    }

    public Linha(String numero, String titulo, String subtitulo) {
        this.numero = numero;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
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

    public static List<Linha> converteMapParaListaLinhas(List<Map<String, Object>> lMapLinhas) {
        List<Linha> lLinhas = new ArrayList<>();
        String numeroAux = null;
        String tituloAux = null;
        String subTituloAux = null;
        for(Map<String, Object> umaLinhaMap : lMapLinhas) {
            for(String umaKey : umaLinhaMap.keySet()) {
                switch(umaKey) {
                    case "numero":
                        numeroAux = umaLinhaMap.get(umaKey).toString();
                        break;
                    case "titulo":
                        tituloAux = umaLinhaMap.get(umaKey).toString();
                        break;
                    case "subtitulo":
                        subTituloAux = umaLinhaMap.get(umaKey).toString();
                        break;
                    default:
                        break;
                }
            }
            lLinhas.add(new Linha(numeroAux, tituloAux, subTituloAux));
        }
        return lLinhas;
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
}
