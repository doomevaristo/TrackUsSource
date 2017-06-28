package com.marcosevaristo.trackussource.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Carro implements Serializable{
    private String id;
    private String longitude;
    private String latitude;
    private String location;

    private static final long serialVersionUID = 1L;

    public Carro() {
    }

    public Carro(String carroId) {
        setId(carroId);
    }

    public Carro(String id, String longitude, String latitude, String location) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static List<Carro> converteMapParaListCarros(Map mapCarros) {
        List<Carro> lCarros = null;
        String id = null;
        String longitude = null;
        String latitude = null;
        String location = null;
        if(mapCarros != null && mapCarros.size() > 0) {
            lCarros = new ArrayList<>();
            for(Object umCarroId : mapCarros.keySet()) {
                Map attrs = (Map) mapCarros.get(umCarroId);
                for(Object umAttr : attrs.keySet()) {
                    String umAttrStr = umAttr.toString();
                    switch (umAttrStr) {
                        case "location":
                            location = attrs.get(umAttr).toString();
                            break;
                        case "latitude":
                            latitude = attrs.get(umAttr).toString();
                            break;
                        case "longitude":
                            longitude = attrs.get(umAttr).toString();
                            break;
                        case "id":
                            id = attrs.get(umAttr).toString();
                            break;
                    }
                }
                lCarros.add(new Carro(id, longitude, latitude, location));
            }
        }

        return lCarros;
    }
}
