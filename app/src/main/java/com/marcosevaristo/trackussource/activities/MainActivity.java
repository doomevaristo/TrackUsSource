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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ListViewCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.trackussource.App;
import com.marcosevaristo.trackussource.R;
import com.marcosevaristo.trackussource.adapters.LinhasAdapter;
import com.marcosevaristo.trackussource.database.QueryBuilder;
import com.marcosevaristo.trackussource.dto.ListaLinhasDTO;
import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.utils.CollectionUtils;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Thread threadSourceSender;
    private Query queryRef;
    private Map carroInfo;
    private String carroId;
    private AppCompatButton botaoIniciarLinha;
    private AppCompatSpinner comboLinhas;
    private ArrayAdapter adapter;
    private ListaLinhasDTO lLinhas;
    private LocationManager mLocationManager;
    private Location location;

    private final String[] PERMISSOES_NECESSARIAS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private final int INT_REQUISICAO_PERMISSOES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.setLinhaAtual(QueryBuilder.getLinhaAtual());
        setupTelaInicial();
    }

    private void setupTelaInicial() {
        setupStatusLinhaIcon();
        while (!possuiPermissoesNecessarias()) {
            ActivityCompat.requestPermissions(this, PERMISSOES_NECESSARIAS, INT_REQUISICAO_PERMISSOES);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        carroId = telephonyManager.getDeviceId();

        FirebaseUtils.startReferences(App.getLinhaAtual(), carroId);

        setupComboLinhas();
        setupBotaoIniciarLinha();
    }

    private void setupStatusLinhaIcon() {
        AppCompatImageView linhaIcon = (AppCompatImageView) findViewById(R.id.statusLinhaIcon);
        if(App.getLinhaAtual() != null) {
            linhaIcon.setImageResource(R.drawable.check);
        } else {
            linhaIcon.setImageResource(R.drawable.close);
        }
    }

    private void setupComboLinhas() {
        comboLinhas = (AppCompatSpinner) findViewById(R.id.comboLinhas);
        comboLinhas.setAdapter(null);

        lLinhas = new ListaLinhasDTO();
        lLinhas.addLinhas(QueryBuilder.getLinhas(null));

        if(CollectionUtils.isEmpty(lLinhas.getlLinhas())) {
            queryRef = FirebaseUtils.getLinhasReference().orderByKey().getRef();
            ValueEventListener evento = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map mapValues = (Map) dataSnapshot.getValue();
                    if (mapValues != null) {
                        lLinhas = new ListaLinhasDTO();
                        lLinhas.addLinhas(Linha.converteMapParaListaLinhas(mapValues));
                        adapter = new ArrayAdapter<String>(App.getAppContext(), R.layout.support_simple_spinner_dropdown_item, lLinhas.getArrayListLinhas());
                        //adapter = new LinhasAdapter(App.getAppContext(), R.layout.item_da_lista_linhas, lLinhas.getlLinhas());
                        adapter.notifyDataSetChanged();
                        comboLinhas.setAdapter(adapter);
                    } else {
                        Toast.makeText(App.getAppContext(), R.string.nenhum_resultado, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            queryRef.addListenerForSingleValueEvent(evento);
        }
    }

    private void setupBotaoIniciarLinha() {
        botaoIniciarLinha = (AppCompatButton) findViewById(R.id.btIniciar);
        botaoIniciarLinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linha linhaSelecionada = (Linha) comboLinhas.getSelectedItem();
                App.setLinhaAtual(linhaSelecionada);
                setupStatusLinhaIcon();
                iniciaThreadSourceSender();
            }
        });
    }

    private void iniciaThreadSourceSender() {
        threadSourceSender.interrupt();
        if(App.getLinhaAtual() != null) {
            threadSourceSender = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        setupLocationSourceSender();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadSourceSender.run();
        }
    }

    private void setupLocationSourceSender() throws InterruptedException {
        location = getLastKnownLocation();
        carroInfo = new HashMap<>();
        Query queryRefSourceSender = FirebaseUtils.getCarroReference();
        while (true) {
            carroInfo.put("latitude", location.getLatitude());
            carroInfo.put("longitude", location.getLongitude());
            queryRefSourceSender.getRef().updateChildren(carroInfo);
            wait(5000);
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

    private boolean possuiPermissoesNecessarias() {
        boolean possuiPermissoes = true;
        for(String umaPermissao : PERMISSOES_NECESSARIAS) {
            possuiPermissoes = possuiPermissoes && ContextCompat.checkSelfPermission(this, umaPermissao) == PackageManager.PERMISSION_GRANTED;
            if(!possuiPermissoes) break;
        }
        return possuiPermissoes;
    }
}
