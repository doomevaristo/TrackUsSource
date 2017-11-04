package com.marcosevaristo.trackussource.activities;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.trackussource.*;
import com.marcosevaristo.trackussource.R;
import com.marcosevaristo.trackussource.adapters.LinhasAdapter;
import com.marcosevaristo.trackussource.database.QueryBuilder;
import com.marcosevaristo.trackussource.dto.ListaLinhasDTO;
import com.marcosevaristo.trackussource.model.Carro;
import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.utils.CollectionUtils;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

import java.util.List;
import java.util.Map;

public class ControleDeLinha extends AppCompatActivity {

    private ProgressBar progressBar;
    private ListView listViewLinhas;

    private ArrayAdapter adapter;
    private ListaLinhasDTO lLinhas;

    private final String[] PERMISSOES_NECESSARIAS = {android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};

    private final int INT_REQUISICAO_PERMISSOES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.marcosevaristo.trackussource.R.layout.activity_controle_de_linha);
        setupTelaInicial();

        if(App.getLinhaAtual() != null) {
            CarroLocationListener.start();
            emiteMensagemLinhaIniciada();
        }
    }

    private void emiteMensagemLinhaIniciada() {
        Toast.makeText(App.getAppContext(), App.getAppContext().getString(R.string.linha_iniciada, App.getLinhaAtual().getNumero()), Toast.LENGTH_LONG).show();
    }

    private void setupTelaInicial() {
        App.setLinhaAtual(QueryBuilder.getLinhaAtual());
        setupStatusLinhaIcon();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        while (!possuiPermissoesNecessarias()) {
            ActivityCompat.requestPermissions(this, PERMISSOES_NECESSARIAS, INT_REQUISICAO_PERMISSOES);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        FirebaseUtils.startReferences();

        setupListViewLinhas();
        setupBotaoIniciarLinha();
    }

    private void setupStatusLinhaIcon() {
        ImageView linhaIcon = (ImageView) findViewById(R.id.statusLinhaIcon);
        if(App.getLinhaAtual() != null) {
            linhaIcon.setImageResource(R.drawable.check);
        } else {
            linhaIcon.setImageResource(R.drawable.close);
        }
    }

    private void setupListViewLinhas() {
        progressBar.setVisibility(View.VISIBLE);
        listViewLinhas = (ListView) findViewById(R.id.listViewLinhas);
        listViewLinhas.setAdapter(null);
        listViewLinhas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(Linha umaLinha : lLinhas.getlLinhas()) {
                    umaLinha.setSelecionada(lLinhas.getlLinhas().indexOf(umaLinha) == position);
                }
                ((LinhasAdapter)listViewLinhas.getAdapter()).selectItem(position);
                ((ListView) parent).invalidateViews();
            }
        });
        lLinhas = new ListaLinhasDTO();
        lLinhas.addLinhas(QueryBuilder.getLinhas(null));

        if(CollectionUtils.isEmpty(lLinhas.getlLinhas())) {
            Query queryRef = FirebaseUtils.getLinhasReference().orderByKey().getRef();
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
                    progressBar.setVisibility(View.GONE);
                    if(lLinhas != null && CollectionUtils.isNotEmpty(lLinhas.getlLinhas())) {
                        for(Linha umaLinha : lLinhas.getlLinhas()) {
                            if(umaLinha.isSelecionada()) {
                                listViewLinhas.setSelection(lLinhas.getlLinhas().indexOf(umaLinha));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            };
            queryRef.addListenerForSingleValueEvent(evento);
        } else {
            adapter = new LinhasAdapter(App.getAppContext(), R.layout.item_da_lista_linhas, lLinhas.getlLinhas());
            listViewLinhas.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
        if(lLinhas != null && CollectionUtils.isNotEmpty(lLinhas.getlLinhas())) {
            for(Linha umaLinha : lLinhas.getlLinhas()) {
                if(umaLinha.isSelecionada()) {
                    listViewLinhas.setSelection(lLinhas.getlLinhas().indexOf(umaLinha));
                }
            }
        }
    }

    private void setupBotaoIniciarLinha() {
        Button botaoIniciarLinha = (Button) findViewById(R.id.btIniciar);
        botaoIniciarLinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linha linhaSelecionada = ((LinhasAdapter)listViewLinhas.getAdapter()).getLinhaSelecionada();
                if(linhaSelecionada != null) {
                    QueryBuilder.atualizaLinhaAtual(linhaSelecionada, App.getCarroId());
                    if(App.getLinhaAtual() != null) {
                        CarroLocationListener.start();
                    }
                    setupStatusLinhaIcon();
                    emiteMensagemLinhaIniciada();
                    minimizar();
                } else {
                    Toast.makeText(App.getAppContext(), R.string.nenhuma_linha_selecionada, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void minimizar() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
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
