package com.marcosevaristo.trackussource;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.firebase.database.Query;
import com.marcosevaristo.trackussource.model.Carro;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CarroLocationListener implements LocationListener {

    private Carro carro;
    private static final Long INTERVALO_UPDATE_LOCAL_EM_MILIS = 5000L;
    private static final Float DISTANCIA_MINIMA_PARA_ATUALIZAR_LOCALIZACAO_EM_METROS = 10.0f;
    private static LocationManager locationManager;
    private static CarroLocationListener listenerInstance;

    public CarroLocationListener(Carro carro) {
        this.carro = carro;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            Query queryRefSourceSender = FirebaseUtils.getCarroReference();
            carro.setLatitude(String.valueOf(location.getLatitude()));
            carro.setLongitude(String.valueOf(location.getLongitude()));
            carro.setLocation(getEndereco(location));
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

    private String getEndereco(Location location) {
        Geocoder geocoder = new Geocoder(App.getAppContext(), Locale.getDefault());

        try {
            List<Address> enderecos = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (enderecos != null) {
                Address umEndereco = enderecos.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < umEndereco.getMaxAddressLineIndex(); i++) {
                    sb.append(umEndereco.getAddressLine(i)).append("");
                }
                return sb.toString();
                //et_lugar.setText(strReturnedAddress.toString());
            } else {
                //et_lugar.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //et_lugar.setText("Canont get Address!");
        }
        return null;
    }

    public static void start() {
        listenerInstance = new CarroLocationListener(new Carro(App.getCarroId()));
        locationManager = (LocationManager) App.getAppContext().getSystemService(App.getAppContext().LOCATION_SERVICE);
        locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(App.getAppContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(App.getAppContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.getLastKnownLocation("gps");
        locationManager.requestLocationUpdates("gps", INTERVALO_UPDATE_LOCAL_EM_MILIS,
                DISTANCIA_MINIMA_PARA_ATUALIZAR_LOCALIZACAO_EM_METROS, listenerInstance);
    }

    public static void stop() {
        if(listenerInstance != null) {
            locationManager.removeUpdates(listenerInstance);
        }
    }
}
