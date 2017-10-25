package com.marcosevaristo.trackussource;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.firebase.database.Query;
import com.marcosevaristo.trackussource.model.Carro;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
}
