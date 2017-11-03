package com.marcosevaristo.trackussource.activities;

import android.app.Activity;
import android.os.Bundle;

import com.marcosevaristo.trackussource.App;
import com.marcosevaristo.trackussource.database.QueryBuilder;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setMunicipio(QueryBuilder.getMunicipioAtual());
        if(App.getMunicipio() == null) {

        } else {

        }
    }

}
