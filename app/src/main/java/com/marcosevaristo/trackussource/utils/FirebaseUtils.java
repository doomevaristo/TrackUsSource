package com.marcosevaristo.trackussource.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marcosevaristo.trackussource.App;

public class FirebaseUtils {

    private static FirebaseDatabase database;
    private static DatabaseReference databaseReferenceMunicipio;
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
                startReferenceCarro();
            }
        }
    }

    private static void startReferenceMunicipios() {
        databaseReferenceMunicipio = getDatabase().getReference().child(NODE_MUNICIPIOS);
    }

    private static void startReferenceCarro() {
        databaseReferenceCarro = getDatabase().getReference().child(NODE_MUNICIPIOS).child(App.getMunicipio().getId().toString())
                .child(NODE_LINHAS).child(App.getLinhaAtual().getNumero()).child(NODE_CARROS).child(App.getCarroId());
    }

    private static void startReferenceLinhas() {
        if(databaseReferenceLinhas == null) {
            databaseReferenceLinhas = getDatabase().getReference().child(NODE_MUNICIPIOS)
                    .child(App.getMunicipio().getId().toString()).child(NODE_LINHAS);
        }
    }

    public static FirebaseDatabase getDatabase() {
        if(database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    public static DatabaseReference getCarroReference() {
        return databaseReferenceCarro;
    }

    public static DatabaseReference getLinhasReference() {
        return databaseReferenceLinhas;
    }

    public static DatabaseReference getMunicipioReference() {
        return databaseReferenceMunicipio;
    }
}
