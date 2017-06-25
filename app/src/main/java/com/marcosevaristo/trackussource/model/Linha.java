package com.marcosevaristo.trackussource.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Linha implements Serializable {
    private Long idSql;
    private Cidade cidade;
    private String numero;
    private String titulo;
    private String subtitulo;
    private List<Carro> carros;
    private boolean ehLinhaAtual = false;

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

    public Long getIdSql() {
        return idSql;
    }

    public void setIdSql(Long idSql) {
        this.idSql = idSql;
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
        for(Object umaLinhaIdObj : lMapLinhas.keySet()) {
            Map<Object, Object> mapUmaLinha = (Map<Object, Object>) lMapLinhas.get(umaLinhaIdObj);
            for(Object umaKeyAux : mapUmaLinha.keySet()) {
                String umaKey = umaKeyAux.toString();
                switch(umaKey) {
                    case "numero":
                        numeroAux = mapUmaLinha.get(umaKeyAux).toString();
                        break;
                    case "titulo":
                        tituloAux = mapUmaLinha.get(umaKeyAux).toString();
                        break;
                    case "subtitulo":
                        subTituloAux = mapUmaLinha.get(umaKeyAux).toString();
                        break;
                    default:
                        break;
                }
            }
            lLinhas.add(new Linha(null, numeroAux, tituloAux, subTituloAux));
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

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public boolean ehLinhaAtual() {
        return ehLinhaAtual;
    }

    public void setEhLinhaAtual(boolean ehLinhaAtual) {
        this.ehLinhaAtual = ehLinhaAtual;
    }
}
