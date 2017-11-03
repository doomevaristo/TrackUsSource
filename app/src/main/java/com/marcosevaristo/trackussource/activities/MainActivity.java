package com.marcosevaristo.trackussource.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.marcosevaristo.trackussource.App;
import com.marcosevaristo.trackussource.database.QueryBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setMunicipio(QueryBuilder.getMunicipioAtual());
        Intent intent;
        if(App.getMunicipio() == null) {
            intent = new Intent(App.getAppContext(), SelecionaMunicipio.class);
        } else {
            intent = new Intent(App.getAppContext(), ControleDeLinha.class);
        }
        startActivity(intent);
    }

}
