package com.marcosevaristo.trackussource.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Municipio {
    private Long id;
    private String nome;
    private List<Linha> lLinhas;
    private boolean ehMunicipioAtual;
    private boolean selecionado = false;

    public Municipio(){}

    public Municipio(Long id) {
        this.id = id;
    }

    public Municipio(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id).append(" - ").append(this.nome);
        return sb.toString();
    }

    public static List<Municipio> converteListMapParaListaMunicipios(List<Map<String, Object>> mapValues) {
        List<Municipio> lMunicipios = new ArrayList<>();
        Long idAux = null;
        String nomeAux = null;
        List<Linha> listLinhas = null;
        Municipio municipioAux;

        for(Map<String, Object> umMunicipio : mapValues) {
            if(umMunicipio != null) {
                for(String umAtributoMun : umMunicipio.keySet()) {
                    switch(umAtributoMun) {
                        case "id":
                            idAux = (Long) umMunicipio.get(umAtributoMun);
                            break;
                        case "nome":
                            nomeAux = umMunicipio.get(umAtributoMun).toString();
                            break;
                        case "linhas":
                            listLinhas = Linha.converteMapParaListaLinhas((Map)umMunicipio.get(umAtributoMun));
                        default:
                            break;
                    }
                }
                municipioAux = new Municipio();
                municipioAux.setId(idAux);
                municipioAux.setNome(nomeAux);
                municipioAux.setlLinhas(listLinhas);
                lMunicipios.add(municipioAux);
            }
        }

        return lMunicipios;
    }
}
