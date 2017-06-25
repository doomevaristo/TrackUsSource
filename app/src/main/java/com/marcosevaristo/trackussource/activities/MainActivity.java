package com.marcosevaristo.trackussource.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.ListViewCompat;
import android.telephony.TelephonyManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.marcosevaristo.trackussource.model.Carro;
import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.utils.CollectionUtils;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Thread threadSourceSender;
    private Query queryRef;
    private Carro carro;
    private String carroId;

    private ContentLoadingProgressBar progressBar;
    private AppCompatButton botaoIniciarLinha;
    private ListViewCompat listViewLinhas;

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
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
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

        setupListViewLinhas();
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

    private void setupListViewLinhas() {
        progressBar.show();
        listViewLinhas = (ListViewCompat) findViewById(R.id.listViewLinhas);
        listViewLinhas.setAdapter(null);
        listViewLinhas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Linha) parent.getItemAtPosition(position)).setSelecionada(true);
            }
        });
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
                        List<Linha> lLinhasAux = Linha.converteMapParaListaLinhas(mapValues);
                        QueryBuilder.insereLinhas(lLinhasAux);
                        for(Linha umaLinha : lLinhasAux) {
                            if(App.getLinhaAtual() == null) break;
                            umaLinha.setEhLinhaAtual(App.getLinhaAtual().getIdSql().equals(umaLinha.getIdSql()));
                        }
                        lLinhas.addLinhas(lLinhasAux);
                        adapter = new LinhasAdapter(App.getAppContext(), R.layout.item_da_lista_linhas, lLinhas.getlLinhas());
                        adapter.notifyDataSetChanged();
                        listViewLinhas.setAdapter(adapter);
                    } else {
                        Toast.makeText(App.getAppContext(), R.string.nenhum_resultado, Toast.LENGTH_LONG).show();
                    }
                    progressBar.hide();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressBar.hide();
                }
            };
            queryRef.addListenerForSingleValueEvent(evento);
        } else {
            adapter = new LinhasAdapter(App.getAppContext(), R.layout.item_da_lista_linhas, lLinhas.getlLinhas());
            listViewLinhas.setAdapter(adapter);
            progressBar.hide();
        }
    }

    private void setupBotaoIniciarLinha() {
        botaoIniciarLinha = (AppCompatButton) findViewById(R.id.btIniciar);
        botaoIniciarLinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linha linhaSelecionada = ((LinhasAdapter)listViewLinhas.getAdapter()).getLinhaSelecionada();
                if(linhaSelecionada != null) {
                    QueryBuilder.atualizaLinhaAtual(linhaSelecionada, carroId);
                    iniciaThreadSourceSender();
                    setupStatusLinhaIcon();
                } else {
                    Toast.makeText(App.getAppContext(), R.string.nenhuma_linha_selecionada, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void iniciaThreadSourceSender() {
        if(threadSourceSender != null && threadSourceSender.isAlive()) {
            threadSourceSender.interrupt();
        }
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
        carro = new Carro();
        Query queryRefSourceSender = FirebaseUtils.getCarroReference();
        while (true) {
            location = getLastKnownLocation();
            carro.setId(carroId);
            carro.setLatitude(String.valueOf(location.getLatitude()));
            carro.setLongitude(String.valueOf(location.getLongitude()));
            carro.setLocation("Teste ab");
            Toast.makeText(this,"Latitude: "+
                    String.valueOf(location.getLatitude())+", Longitude: "+String.valueOf(location.getLongitude()), Toast.LENGTH_LONG).show();
            queryRefSourceSender.getRef().setValue(carro);
            Thread.sleep(5000);
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
