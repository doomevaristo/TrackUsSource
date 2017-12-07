package com.marcosevaristo.trackussource.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marcosevaristo.trackussource.R;
import com.marcosevaristo.trackussource.app.App;
import com.marcosevaristo.trackussource.model.Municipio;

import java.util.ArrayList;
import java.util.List;

public class MunicipiosAdapter extends ArrayAdapter<Municipio> {
    private List<Municipio> lMunicipios = new ArrayList<>();
    private int layoutResId;
    private Context ctx;
    private int posicaoSelecionada = -1;

    public MunicipiosAdapter(Context ctx, int layoutResId, List<Municipio> lMunicipios) {
        super(ctx, layoutResId, lMunicipios);
        this.layoutResId = layoutResId;
        this.ctx = ctx;
        this.lMunicipios = lMunicipios;
    }

    @Override
    public int getCount() {
        return lMunicipios.size();
    }

    @Override
    public Municipio getItem(int pos) {
        return lMunicipios.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MunicipioHolder municipioHolder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutResId, parent, false);
            municipioHolder = new MunicipioHolder();
            TextView textView = (TextView)view.findViewById(R.id.municipioText);
            municipioHolder.texto = textView;

            view.setTag(municipioHolder);
        } else {
            municipioHolder = (MunicipioHolder) view.getTag();
        }

        Municipio municipio = lMunicipios.get(position);
        if(municipio != null) {
            municipioHolder.texto.setText(municipio.toString());

            if((position == posicaoSelecionada) || (posicaoSelecionada == -1 && municipio.isEhMunicipioAtual())) {
                view.setBackgroundColor(App.getAppContext().getResources().getColor(R.color.selectedItem));
            } else {
                view.setBackgroundColor(Color.TRANSPARENT);
            }
        }
        return view;
    }

    public void selectItem(int pos) {
        posicaoSelecionada = pos;
    }

    public Municipio getMunicipioSelecionado() {
        for(Municipio umMunicipio : lMunicipios) {
            if(umMunicipio.isSelecionado()) {
                return umMunicipio;
            }
        }
        return null;
    }

    private static class MunicipioHolder {
        TextView texto;
    }
}
