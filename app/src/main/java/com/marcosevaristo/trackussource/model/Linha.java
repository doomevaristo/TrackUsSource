package com.marcosevaristo.trackussource.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Linha implements Serializable {
    private Cidade cidade;
    private String numero;
    private String titulo;
    private String subtitulo;
    private List<Carro> carros;
    private boolean ehFavorito = false;

    private static final long serialVersionUID = 1L;

    public Linha() {
    }

    public Linha(List<Carro> carros, String numero, String titulo, String subtitulo) {
        this.carros = carros;
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

    public List<Carro> getCarros() {
        return carros;
    }

    public void setCarros(List<Carro> carros) {
        this.carros = carros;
    }

    public static List<Linha> converteMapParaListaLinhas(Map lMapLinhas) {
        List<Linha> lLinhas = new ArrayList<>();
        String numeroAux = null;
        String tituloAux = null;
        String subTituloAux = null;
        String location = null;
        String latitude = null;
        String longitude = null;
        String id = null;
        List<Carro> lCarrosAux = new ArrayList<>();
        for(Object umaKeyAux : lMapLinhas.keySet()) {
            String umaKey = umaKeyAux.toString();
            switch(umaKey) {
                case "numero":
                    numeroAux = lMapLinhas.get(umaKeyAux).toString();
                    break;
                case "titulo":
                    tituloAux = lMapLinhas.get(umaKeyAux).toString();
                    break;
                case "subtitulo":
                    subTituloAux = lMapLinhas.get(umaKeyAux).toString();
                    break;
                case "carros":
                    Collection<Map> mapCarros = ((Map) lMapLinhas.get(umaKeyAux)).values();
                    for(Map umCarroMap : mapCarros) {
                        for(Object umKey : umCarroMap.keySet()) {
                            String umKeyStr = umKey.toString();
                            switch (umKeyStr) {
                                case "location":
                                    location = umCarroMap.get(umKey).toString();
                                    break;
                                case "latitude":
                                    latitude = umCarroMap.get(umKey).toString();
                                    break;
                                case "longitude":
                                    longitude = umCarroMap.get(umKey).toString();
                                    break;
                                case "id":
                                    id = umCarroMap.get(umKey).toString();
                                    break;
                                default:
                                    break;
                            }
                        }
                        lCarrosAux.add(new Carro(id, longitude, latitude, location));
                    }
                    break;
                default:
                    break;
            }
        }
        Linha linhaAux = new Linha(lCarrosAux, numeroAux, tituloAux, subTituloAux);
        linhaAux.setCarros(lCarrosAux);
        lLinhas.add(linhaAux);
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

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public boolean ehFavorito() {
        return ehFavorito;
    }

    public void setEhFavorito(boolean ehFavorito) {
        this.ehFavorito = ehFavorito;
    }
}
