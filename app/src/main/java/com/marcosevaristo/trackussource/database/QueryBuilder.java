package com.marcosevaristo.trackussource.database;

import android.database.Cursor;

import com.marcosevaristo.trackussource.App;
import com.marcosevaristo.trackussource.model.Cidade;
import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.utils.CollectionUtils;
import com.marcosevaristo.trackussource.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private static SQLiteHelper sqLiteHelper = App.getSqLiteHelper();

    private QueryBuilder() {}

    public static List<Linha> getLinhas(String nroLinha) {
        List<Linha> lLinhas = new ArrayList<>();
        Linha linhaAux;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllLinhas(nroLinha), null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        while(cursor.moveToNext()) {
            linhaAux = new Linha();
            linhaAux.setNumero(cursor.getString(0));
            linhaAux.setTitulo(cursor.getString(1));
            linhaAux.setSubtitulo(cursor.getString(2));
            linhaAux.setCidade(new Cidade(cursor.getString(3)));
            lLinhas.add(linhaAux);
        }

        cursor.close();
        return lLinhas;
    }

    private static String getSelectAllLinhas(String nroLinha) {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TLinhas.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TLinhas.TABLE_NAME).append(" LIN ");
        if(StringUtils.isNotBlank(nroLinha)) {
            sb.append(" WHERE ").append(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO).append(" LIKE '%").append(nroLinha).append("%' ");
        }
        sb.append(" ORDER BY ").append(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO).append(" DESC");
        return sb.toString();
    }

    private static boolean colunasInformadasClausulasNaoInformadas(String[] columns, List<Cidade> lCidades) {
        return CollectionUtils.isEmpty(lCidades) && columns != null && columns.length > 0;
    }

}
