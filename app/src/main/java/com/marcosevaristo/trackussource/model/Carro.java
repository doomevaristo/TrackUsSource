package com.marcosevaristo.trackussource.model;

import java.io.Serializable;

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
}
