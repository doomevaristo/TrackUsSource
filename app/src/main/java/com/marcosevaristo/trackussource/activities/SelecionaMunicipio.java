package com.marcosevaristo.trackussource.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.trackussource.App;
import com.marcosevaristo.trackussource.CarroLocationListener;
import com.marcosevaristo.trackussource.R;
import com.marcosevaristo.trackussource.adapters.MunicipiosAdapter;
import com.marcosevaristo.trackussource.database.QueryBuilder;
import com.marcosevaristo.trackussource.model.Municipio;
import com.marcosevaristo.trackussource.utils.CollectionUtils;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

public class SelecionaMunicipio extends AppCompatActivity {

    private List<Municipio> lMunicipios;
    private ListView listViewMunicipios;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleciona_municipio);
        setupListMunicipios();
        setupBotaoSelecionarMunicipio();
    }

    private void setupListMunicipios() {
        listViewMunicipios = (ListView) findViewById(R.id.listViewMunicipios);
        listViewMunicipios.setAdapter(null);
        listViewMunicipios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(Municipio umMunicipio : lMunicipios) {
                    umMunicipio.setSelecionado(lMunicipios.indexOf(umMunicipio) == position);
                }
                ((MunicipiosAdapter)listViewMunicipios.getAdapter()).selectItem(position);
                ((ListView) parent).invalidateViews();
            }
        });
        lMunicipios = new ArrayList<>();
        lMunicipios.addAll(QueryBuilder.getMunicipios(null));

        if(CollectionUtils.isEmpty(lMunicipios)) {
            Query queryRef = FirebaseUtils.getMunicipiosReference(null).orderByKey().getRef();
            ValueEventListener evento = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null && dataSnapshot.getChildren() != null) {
                        lMunicipios = new ArrayList<>();
                        for(DataSnapshot umDataSnapshot : dataSnapshot.getChildren()) {
                            lMunicipios.add(umDataSnapshot.getValue(Municipio.class));
                            QueryBuilder.insereMunicipios(lMunicipios);
                            if(App.getMunicipio() != null) {
                                for(Municipio umMunicipio : lMunicipios) {
                                    if(App.getMunicipio().getId().equals(umMunicipio.getId())) {
                                        umMunicipio.setEhMunicipioAtual(true);
                                        break;
                                    }
                                }
                            }
                            adapter = new MunicipiosAdapter(App.getAppContext(), R.layout.item_da_lista_municipios, lMunicipios);
                            adapter.notifyDataSetChanged();
                            listViewMunicipios.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(App.getAppContext(), R.string.nenhum_resultado, Toast.LENGTH_LONG).show();
                    }
                    setaSelecao();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            queryRef.addListenerForSingleValueEvent(evento);
        } else {
            adapter = new MunicipiosAdapter(App.getAppContext(), R.layout.item_da_lista_municipios, lMunicipios);
            listViewMunicipios.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        setaSelecao();
    }

    private void setaSelecao() {
        if(lMunicipios != null && CollectionUtils.isNotEmpty(lMunicipios)) {
            for(Municipio umMinicipio : lMunicipios) {
                if(umMinicipio.isSelecionado()) {
                    listViewMunicipios.setSelection(lMunicipios.indexOf(umMinicipio));
                }
            }
        }
    }

    private void setupBotaoSelecionarMunicipio() {
        Button botaoSelecionar = (Button) findViewById(R.id.btSelecionar);
        botaoSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Municipio municipioSelecionado = ((MunicipiosAdapter)listViewMunicipios.getAdapter()).getMunicipioSelecionado();
                if(municipioSelecionado != null) {
                    QueryBuilder.atualizaMunicipioAtual(municipioSelecionado);
                    if(App.getMunicipio() != null) {
                        CarroLocationListener.stop();
                        Toast.makeText(App.getAppContext(), R.string.iniciar_linha, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(App.getAppContext(), ControleDeLinha.class));
                    }
                } else {
                    Toast.makeText(App.getAppContext(), R.string.nenhum_municipio_selecionado, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
