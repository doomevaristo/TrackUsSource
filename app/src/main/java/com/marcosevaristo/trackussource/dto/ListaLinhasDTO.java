package com.marcosevaristo.trackussource.dto;

import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ListaLinhasDTO {

    private List<Linha> lLinhas;

    public void addLinhas(List<Linha> lista) {
        if(CollectionUtils.isEmpty(lLinhas)) {
            lLinhas = new ArrayList<>();
        }
        for(Linha umaLinha : lista) {
            if(findLinhaByNumeroOuTitulo(umaLinha.getNumero().toString()) == null
                    && this.findLinhaByNumeroOuTitulo(umaLinha.getTitulo()) == null) {
                lLinhas.add(umaLinha);
            }
        }
    }

    public List<Linha> getlLinhas() {
        return lLinhas;
    }

    public ArrayList<String> getArrayListLinhas() {
        List<String> lAux = new ArrayList<String>();
        for(Linha umaLinha : this.getlLinhas()) {
            lAux.add(umaLinha.toString());
        }
        return (ArrayList<String>) lAux;
    }

    /**
     * Retorna a linha da lista que tenha o número ou título informado como argumento. Retorna null se não encontrar.
     * @param arg
     * @return
     */
    private Linha findLinhaByNumeroOuTitulo(String arg) {
        if(CollectionUtils.isNotEmpty(this.lLinhas)) {
            for(Linha umaLinha : this.lLinhas) {
                if(umaLinha.getNumero().toString().equals(arg) || umaLinha.getTitulo().equals(arg)) {
                    return umaLinha;
                }
            }
        }
        return null;
    }
}
