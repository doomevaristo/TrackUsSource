package com.marcosevaristo.trackussource.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.marcosevaristo.trackussource.R;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Query queryRef;
    private Map carro;
    LocationManager mLocationManager;
    Location location;
    private final String[] PERMISSOES_NECESSARIAS = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final int INT_REQUISICAO_PERMISSOES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupTelaInicial();
        FirebaseUtils.startReferenceLinhas();
        try {
            setupLocationSourceSender();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setupTelaInicial() {
        AppCompatSpinner comboLinhas = (AppCompatSpinner) findViewById(R.id.comboLinhas);
        String[] arrayLinhas = new String[]{"teste"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayLinhas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboLinhas.setAdapter(adapter);
    }

    private void setupLocationSourceSender() throws InterruptedException {
        if (!possuiPermissoesNecessarias()) {
            ActivityCompat.requestPermissions(this, PERMISSOES_NECESSARIAS, INT_REQUISICAO_PERMISSOES);
        } else {
            location = getLastKnownLocation();
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String uniqueId = telephonyManager.getDeviceId();
            carro = new HashMap<>();
            queryRef = FirebaseUtils.getLinhasReference().orderByChild("carros").equalTo(uniqueId);
            while (true) {
                carro.put("latitude", location.getLatitude());
                carro.put("longitude", location.getLongitude());
                queryRef.getRef().updateChildren(carro);
                wait(5000);
            }
        }
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location melhorLocalizacao = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (melhorLocalizacao == null || l.getAccuracy() < melhorLocalizacao.getAccuracy()) {
                melhorLocalizacao = l;
            }
        }
        return melhorLocalizacao;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case INT_REQUISICAO_PERMISSOES: {
                try {
                    setupLocationSourceSender();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }

    private boolean possuiPermissoesNecessarias() {
        boolean possuiPermissoes = true;
        for(String umaPermissao : PERMISSOES_NECESSARIAS) {
            possuiPermissoes = possuiPermissoes && ContextCompat.checkSelfPermission(this, umaPermissao) == PackageManager.PERMISSION_GRANTED;
            if(!possuiPermissoes) break;
        }
        return possuiPermissoes;
    }
}
