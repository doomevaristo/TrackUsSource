package com.marcosevaristo.trackussource.dto;

import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.model.Municipio;
import com.marcosevaristo.trackussource.utils.CollectionUtils;
import com.marcosevaristo.trackussource.utils.NumberUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ListaMunicipiosDTO {

    private List<Municipio> lMunicipios;

    public void addMunicipios(List<Municipio> lista) {
        if(CollectionUtils.isNotEmpty(lista)) {
            if(CollectionUtils.isEmpty(lMunicipios)) {
                lMunicipios = new ArrayList<>();
            }
            for(Municipio umMunicipio : lista) {
                if(findMunicipioByNomeOuID(umMunicipio.getId().toString()) == null
                        && findMunicipioByNomeOuID(umMunicipio.getNome()) == null) {
                    lMunicipios.add(umMunicipio);
                }
            }
        }
    }

    public List<Municipio> getlMunicipios() {
        return lMunicipios;
    }

    public ArrayList<String> getArrayListMunicipios() {
        List<String> lAux = new ArrayList<String>();
        for(Municipio umMunicipio : this.getlMunicipios()) {
            lAux.add(umMunicipio.toString());
        }
        return (ArrayList<String>) lAux;
    }

    private Municipio findMunicipioByNomeOuID(String arg) {
        if(CollectionUtils.isNotEmpty(this.lMunicipios)) {
            for(Municipio umMunicipio : this.lMunicipios) {
                if((NumberUtils.isNumber(arg) && umMunicipio.getId().equals(NumberUtils.toLong(arg))) || umMunicipio.getNome().equals(arg)) {
                    return umMunicipio;
                }
            }
        }
        return null;
    }
}
