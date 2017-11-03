package com.marcosevaristo.trackussource.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.marcosevaristo.trackussource.adapters.LinhasAdapter;
import com.marcosevaristo.trackussource.adapters.MunicipiosAdapter;
import com.marcosevaristo.trackussource.database.QueryBuilder;
import com.marcosevaristo.trackussource.dto.ListaLinhasDTO;
import com.marcosevaristo.trackussource.dto.ListaMunicipiosDTO;
import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.model.Municipio;
import com.marcosevaristo.trackussource.utils.CollectionUtils;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

import java.util.List;
import java.util.Map;

public class SelecionaMunicipio extends AppCompatActivity {

    private ListaMunicipiosDTO lMunicipios;
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
                for(Municipio umMunicipio : lMunicipios.getlMunicipios()) {
                    umMunicipio.setSelecionado(lMunicipios.getlMunicipios().indexOf(umMunicipio) == position);
                }
                ((MunicipiosAdapter)listViewMunicipios.getAdapter()).selectItem(position);
                ((ListView) parent).invalidateViews();
            }
        });
        lMunicipios = new ListaMunicipiosDTO();
        lMunicipios.addMunicipios(QueryBuilder.getMunicipios(null));

        if(CollectionUtils.isEmpty(lMunicipios.getlMunicipios())) {
            Query queryRef = FirebaseUtils.getMunicipiosReference().orderByKey().getRef();
            ValueEventListener evento = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map mapValues = (Map) dataSnapshot.getValue();
                    if (mapValues != null) {
                        lMunicipios = new ListaMunicipiosDTO();
                        List<Municipio> lMunicipiosAux = Municipio.converteMapParaListaMunicipios(mapValues);
                        for(Municipio umMunicipio : lMunicipiosAux) {
                            if(App.getLinhaAtual() == null) break;
                            umMunicipio.setEhMunicipioAtual(App.getMunicipio().getId().equals(umMunicipio.getId()));
                        }
                        lMunicipios.addMunicipios(lMunicipiosAux);
                        adapter = new MunicipiosAdapter(App.getAppContext(), R.layout.item_da_lista_linhas, lMunicipios.getlMunicipios());
                        adapter.notifyDataSetChanged();
                        listViewMunicipios.setAdapter(adapter);
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
            adapter = new MunicipiosAdapter(App.getAppContext(), R.layout.item_da_lista_municipios, lMunicipios.getlMunicipios());
            listViewMunicipios.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        setaSelecao();
    }

    private void setaSelecao() {
        if(lMunicipios != null && CollectionUtils.isNotEmpty(lMunicipios.getlMunicipios())) {
            for(Municipio umMinicipio : lMunicipios.getlMunicipios()) {
                if(umMinicipio.isSelecionado()) {
                    listViewMunicipios.setSelection(lMunicipios.getlMunicipios().indexOf(umMinicipio));
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
                        startActivity(new Intent(App.getAppContext(), SelecionaMunicipio.class));
                    }
                } else {
                    Toast.makeText(App.getAppContext(), R.string.nenhum_municipio_selecionado, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
