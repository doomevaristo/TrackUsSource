package com.marcosevaristo.trackussource.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marcosevaristo.trackussource.App;
import com.marcosevaristo.trackussource.model.Linha;

public class FirebaseUtils {

    private static FirebaseDatabase database;
    private static DatabaseReference databaseReferenceMunicipios;
    private static DatabaseReference databaseReferenceCarro;
    private static DatabaseReference databaseReferenceLinhas;

    private static final String NODE_MUNICIPIOS = "municipios";
    private static final String NODE_LINHAS = "linhas";
    private static final String NODE_CARROS = "carros";

    public static void startReferences() {
        startReferenceMunicipios();
        if(App.getMunicipio() != null) {
            startReferenceLinhas();
            if(App.getLinhaAtual() != null && StringUtils.isNotBlank(App.getCarroId())) {
                startReferenceCarro(App.getLinhaAtual().getNumero(), App.getCarroId());
            }
        }
    }

    public static void startReferenceMunicipios() {
        databaseReferenceMunicipios = getDatabase().getReference().child(NODE_MUNICIPIOS);
    }

    public static void startReferenceCarro(String numeroLinha, String carroId) {
        databaseReferenceCarro = getDatabase().getReference().child(NODE_MUNICIPIOS).child(App.getMunicipio().getId().toString())
                .child(NODE_LINHAS).child(numeroLinha).child(NODE_CARROS).child(carroId);
    }

    private static void startReferenceLinhas() {
        databaseReferenceLinhas = getDatabase().getReference().child(NODE_MUNICIPIOS)
                .child(App.getMunicipio().getId().toString()).child(NODE_LINHAS);
    }

    public static FirebaseDatabase getDatabase() {
        if(database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    public static DatabaseReference getCarroReference() {
        if(databaseReferenceCarro == null) startReferenceCarro(App.getLinhaAtual().getNumero(), App.getCarroId());
        return databaseReferenceCarro;
    }

    public static DatabaseReference getLinhasReference() {
        if(databaseReferenceLinhas == null) startReferenceLinhas();
        return databaseReferenceLinhas;
    }

    public static DatabaseReference getMunicipiosReference() {
        if(databaseReferenceMunicipios == null) startReferenceMunicipios();
        return databaseReferenceMunicipios;
    }
}
