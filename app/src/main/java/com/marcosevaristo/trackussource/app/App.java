package com.marcosevaristo.trackussource.app;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import com.marcosevaristo.trackussource.database.SQLiteHelper;
import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.model.Municipio;

public class App extends Application {
    private static Context context;
    private static SQLiteHelper sqLiteHelper;
    private static Linha linhaAtual;
    private static String carroId;
    private static Municipio municipio;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sqLiteHelper = SQLiteHelper.getInstance(context);
        carroId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static Context getAppContext() {
        return App.context;
    }

    public static SQLiteHelper getSqLiteHelper() {
        return sqLiteHelper;
    }

    public static Linha getLinhaAtual() {
        return linhaAtual;
    }

    public static void setLinhaAtual(Linha linhaAtual) {
        App.linhaAtual = linhaAtual;
    }

    public static String getCarroId() {
        return carroId;
    }

    public static Municipio getMunicipio() {
        return municipio;
    }
    public static void setMunicipio(Municipio municipio) {
        App.municipio = municipio;
    }

    public static void toast(int stringID, String... params) {
        Toast.makeText(context, context.getString(stringID, params), Toast.LENGTH_SHORT).show();
    }
}
