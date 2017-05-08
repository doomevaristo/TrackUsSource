package com.marcosevaristo.trackussource.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.google.firebase.database.Query;
import com.marcosevaristo.trackussource.R;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Query queryRef;
    private Map carro;
    private final String[] permissoesNecessarias = {Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            setupLocationSourceSender();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setupLocationSourceSender() throws InterruptedException {
        FirebaseUtils.startReferenceLinhas();
        if(possuiPermissoesNecessarias()) {
            TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String simNumberStr = telemamanger.getLine1Number();
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            carro = new HashMap<>();
            queryRef = FirebaseUtils.getLinhasReference().orderByChild("carros").equalTo(simNumberStr);
            while (true) {
                carro.put("latitude", location.getLatitude());
                carro.put("longitude", location.getLongitude());
                queryRef.getRef().updateChildren(carro);
                wait(5000);
            }
        } else {

        }

    }

    private boolean possuiPermissoesNecessarias() {
        boolean possuiPermissoes = false;
        for(String umaPermissao : permissoesNecessarias) {
            possuiPermissoes = possuiPermissoes && ContextCompat.checkSelfPermission(this, umaPermissao) == 0;
            if(!possuiPermissoes) break;
        }
        return possuiPermissoes;
    }
}
