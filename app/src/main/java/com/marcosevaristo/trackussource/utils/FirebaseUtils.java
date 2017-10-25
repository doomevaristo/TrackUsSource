package com.marcosevaristo.trackussource.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marcosevaristo.trackussource.constants.FirebaseConstants;
import com.marcosevaristo.trackussource.model.Linha;

public class FirebaseUtils {

    private static FirebaseDatabase database;
    private static DatabaseReference databaseReferenceCarro;
    private static DatabaseReference databaseReferenceLinhas;

    public static void startReferences(Linha linhaAtual, String idCarro) {
        if(linhaAtual != null) {
            startReferenceCarro(linhaAtual, idCarro);
        }
        startReferenceLinhas();
    }

    public static void startReferenceCarro(Linha linhaAtual, String idCarro) {
        databaseReferenceCarro = getDatabase().getReference().child(FirebaseConstants.NODE_MUNICIPIOS)
                .child(FirebaseConstants.NODE_LINHAS).child(linhaAtual.getNumero()).child(FirebaseConstants.NODE_CARROS).child(idCarro);
    }

    public static void startReferenceLinhas() {
        if(databaseReferenceLinhas == null) {
            databaseReferenceLinhas = getDatabase().getReference().child(FirebaseConstants.NODE_MUNICIPIOS)
                    .child(FirebaseConstants.NODE_LINHAS);
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
}
