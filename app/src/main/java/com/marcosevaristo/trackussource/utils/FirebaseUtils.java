package com.marcosevaristo.trackussource.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marcosevaristo.trackussource.App;

public class FirebaseUtils {

    private static FirebaseDatabase database;

    private static final String NODE_MUNICIPIOS = "municipios";
    private static final String NODE_LINHAS = "linhas";
    private static final String NODE_CARROS = "carros";

    public static DatabaseReference getLinhasReference(String linhaID) {
        DatabaseReference databaseReferenceLinhas = getMunicipiosReference(App.getMunicipio().getId()).child(NODE_LINHAS);
        if(StringUtils.isNotBlank(linhaID)) {
            databaseReferenceLinhas = databaseReferenceLinhas.child(linhaID);
        }
        return databaseReferenceLinhas;
    }

    public static DatabaseReference getMunicipiosReference(String municipioID) {
        DatabaseReference databaseReferenceMunicipios = getDatabase().getReference().child(NODE_MUNICIPIOS);
        if(StringUtils.isNotBlank(municipioID)) {
            databaseReferenceMunicipios = databaseReferenceMunicipios.child(municipioID);
        }
        return databaseReferenceMunicipios;
    }

    public static DatabaseReference getCarroReference(String linhaID, String carroID) {
        DatabaseReference databaseReferenceCarro = null;
        if(StringUtils.isNotBlank(App.getMunicipio().getId())
                && StringUtils.isNotBlank(linhaID)
                && StringUtils.isNotBlank(App.getCarroId())) {
            databaseReferenceCarro = getLinhasReference(linhaID).child(NODE_CARROS).child(carroID);
        }
        return databaseReferenceCarro;
    }

    public static FirebaseDatabase getDatabase() {
        if(database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }
}
