package com.marcosevaristo.trackussource.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    private static FirebaseDatabase database;
    private static DatabaseReference databaseReferenceLinhas;

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

    public static DatabaseReference getLinhasReference() {
        return databaseReferenceLinhas;
    }
}
