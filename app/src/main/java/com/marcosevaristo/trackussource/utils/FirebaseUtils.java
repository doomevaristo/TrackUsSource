package com.marcosevaristo.trackussource.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marcosevaristo.trackussource.model.Linha;

public class FirebaseUtils {

    private static FirebaseDatabase database;
    private static DatabaseReference databaseReferenceCarro;
    private static DatabaseReference databaseReferenceLinhas;

    public static void startReferenceCarro(Linha linhaAtual, String idCarro) {
        if(databaseReferenceCarro == null) {
            databaseReferenceCarro = getDatabase().getReference().child("linhas").child(linhaAtual.getNumero()).child("carros").child(idCarro);
        }
    }

    public static void startReferenceLinhas() {
        if(databaseReferenceLinhas == null) {
            databaseReferenceLinhas = getDatabase().getReference().child("linhas");
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
