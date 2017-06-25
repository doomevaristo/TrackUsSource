package com.marcosevaristo.trackussource;

import android.app.Application;
import android.content.Context;

import com.marcosevaristo.trackussource.database.SQLiteHelper;
import com.marcosevaristo.trackussource.model.Linha;

public class App extends Application {
    private static Context context;
    private static SQLiteHelper sqLiteHelper;
    private static Linha linhaAtual;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sqLiteHelper = SQLiteHelper.getInstance(context);
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
}
