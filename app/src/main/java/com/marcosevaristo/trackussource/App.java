package com.marcosevaristo.trackussource;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.marcosevaristo.trackussource.database.SQLiteHelper;
import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;

public class App extends Application {
    private static Context context;
    private static SQLiteHelper sqLiteHelper;
    private static Linha linhaAtual;
    private static String carroId;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sqLiteHelper = SQLiteHelper.getInstance(context);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        carroId = telephonyManager.getDeviceId();
        FirebaseUtils.startReferenceCarro(App.getLinhaAtual(), carroId);
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
