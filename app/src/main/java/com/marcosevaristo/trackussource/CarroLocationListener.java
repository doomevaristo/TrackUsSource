package com.marcosevaristo.trackussource;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.firebase.database.Query;
import com.marcosevaristo.trackussource.model.Carro;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

public class CarroLocationListener implements LocationListener {

    Carro carro;

    public CarroLocationListener(Carro carro) {
        this.carro = carro;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            Query queryRefSourceSender = FirebaseUtils.getCarroReference();
            carro.setLatitude(String.valueOf(location.getLatitude()));
            carro.setLongitude(String.valueOf(location.getLongitude()));
            carro.setLocation("Teste axc");
            queryRefSourceSender.getRef().setValue(carro);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
