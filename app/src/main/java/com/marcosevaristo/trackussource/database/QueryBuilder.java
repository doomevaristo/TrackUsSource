package com.marcosevaristo.trackussource.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.trackussource.App;
import com.marcosevaristo.trackussource.model.Municipio;
import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.utils.FirebaseUtils;
import com.marcosevaristo.trackussource.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBuilder {

    private static SQLiteHelper sqLiteHelper = App.getSqLiteHelper();
    private static Query queryRefLinhaAtualOld;
    private static Query queryRefNovaLinha;

    private QueryBuilder() {}

    public static List<Linha> getLinhas(String nroLinha) {
        List<Linha> lLinhas = new ArrayList<>();
        Linha linhaAux;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllLinhas(nroLinha), null);
        if(cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                linhaAux = new Linha();
                linhaAux.setIdSql(cursor.getLong(0));
                linhaAux.setNumero(cursor.getString(1));
                linhaAux.setTitulo(cursor.getString(2));
                linhaAux.setSubtitulo(cursor.getString(3));
                linhaAux.setMunicipio(new Municipio(cursor.getLong(4)));
                if(App.getLinhaAtual() != null) {
                    linhaAux.setEhLinhaAtual(App.getLinhaAtual().getIdSql().equals(linhaAux.getIdSql()));
                }
                lLinhas.add(linhaAux);
                cursor.moveToNext();
            }

            cursor.close();
        }

        return lLinhas;
    }

    public static List<Municipio> getMunicipios(Long municipioID) {
        List<Municipio> lLinhas = new ArrayList<>();
        Municipio municipioAux;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllMunicipios(municipioID), null);
        if(cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                municipioAux = new Municipio();
                municipioAux.setId(cursor.getLong(0));
                municipioAux.setNome(cursor.getString(1));
                if(App.getMunicipio() != null) {
                    municipioAux.setEhMunicipioAtual(App.getMunicipio().getId().equals(municipioAux.getId()));
                }
                lLinhas.add(municipioAux);
                cursor.moveToNext();
            }

            cursor.close();
        }
        return lLinhas;
    }

    private static String getSelectAllMunicipios(Long municipioID) {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TMunicipios.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TMunicipios.TABLE_NAME).append(" MUN ");
        if(municipioID != null) {
            sb.append(" WHERE ").append(SQLiteObjectsHelper.TMunicipios._ID).append(" = ").append(municipioID.toString());
        }
        sb.append(" ORDER BY ").append(SQLiteObjectsHelper.TMunicipios.COLUMN_MUNNOME).append(" DESC");
        return sb.toString();
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

    public static Municipio getMunicipioAtual() {
        Municipio municipioAux = null;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllMunicipioAtual(), null);
        if(cursor != null) {
            cursor.moveToFirst();

            municipioAux = new Municipio();
            municipioAux.setId(cursor.getLong(0));
            municipioAux.setNome(cursor.getString(1));

            cursor.close();
        }

        return municipioAux;
    }

    private static String getSelectAllMunicipioAtual() {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TMunicipios.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TMunicipioAtual.TABLE_NAME).append(" MUA ");
        sb.append(" INNER JOIN ").append(SQLiteObjectsHelper.TMunicipios.TABLE_NAME).append(" MUN ON MUN.");
        sb.append(SQLiteObjectsHelper.TMunicipios._ID).append(" = MUA.").append(SQLiteObjectsHelper.TMunicipioAtual.COLUMN_MUNICIPIOID);
        return sb.toString();
    }

    public static Linha getLinhaAtual() {
        Linha linhaAux = null;
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(getSelectAllLinhaAtual(), null);
        if(cursor != null) {
            cursor.moveToFirst();
            linhaAux = new Linha();
            linhaAux.setIdSql(cursor.getLong(0));
            linhaAux.setNumero(cursor.getString(1));
            linhaAux.setTitulo(cursor.getString(2));
            linhaAux.setSubtitulo(cursor.getString(3));
            linhaAux.setMunicipio(new Municipio(cursor.getLong(4)));

            cursor.close();
        }

        return linhaAux;
    }

    private static String getSelectAllLinhaAtual() {
        StringBuilder sb = new StringBuilder("SELECT ").append(SQLiteObjectsHelper.TLinhas.getInstance().getColunasParaSelect()).append(" FROM ");
        sb.append(SQLiteObjectsHelper.TLinhaAtual.TABLE_NAME).append(" LIA ");
        sb.append(" INNER JOIN ").append(SQLiteObjectsHelper.TLinhas.TABLE_NAME).append(" LIN ON LIN.");
        sb.append(SQLiteObjectsHelper.TLinhas._ID).append(" = LIA.").append(SQLiteObjectsHelper.TLinhaAtual.COLUMN_LINHAID);
        return sb.toString();
    }

    public static void insereLinhas(List<Linha> lLinhas) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        db.beginTransaction();
        for (Linha umaLinha : lLinhas) {
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO, umaLinha.getNumero());
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_TITULO, umaLinha.getTitulo());
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_SUBTITULO, umaLinha.getSubtitulo());
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_CIDADE, umaLinha.getMunicipio().getId());
            umaLinha.setIdSql(db.insert(SQLiteObjectsHelper.TLinhas.TABLE_NAME, null, values));
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void atualizaLinhaAtual(Linha novaLinha, String carroId) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        Linha linhaAtualAux = getLinhas(novaLinha.getNumero()).get(0);
        Linha linhaAtualOld = getLinhaAtual();
        ContentValues values = new ContentValues();
        values.put(SQLiteObjectsHelper.TLinhaAtual.COLUMN_LINHAID, linhaAtualAux.getIdSql());
        if(linhaAtualOld == null) {
            db.beginTransaction();
            linhaAtualAux.setIdSql(db.insert(SQLiteObjectsHelper.TLinhaAtual.TABLE_NAME, null, values));
        } else {
            StringBuilder whereClause = new StringBuilder();
            whereClause.append(SQLiteObjectsHelper.TLinhaAtual.COLUMN_LINHAID).append(" = ?");
            db.beginTransaction();
            db.update(SQLiteObjectsHelper.TLinhaAtual.TABLE_NAME, values, whereClause.toString(), new String[]{linhaAtualOld.getIdSql().toString()});
        }

        alteraLinhaAtualFirebase(novaLinha, carroId);

        App.setLinhaAtual(linhaAtualAux);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private static void alteraLinhaAtualFirebase(final Linha novaLinha, final String carroId) {
        queryRefLinhaAtualOld = FirebaseUtils.getCarroReference();
        FirebaseUtils.startReferenceCarro(novaLinha.getNumero(), carroId);
        queryRefNovaLinha = FirebaseUtils.getCarroReference();

        if(queryRefLinhaAtualOld != null) { //Alterou linha
            queryRefLinhaAtualOld.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null) {
                        queryRefNovaLinha.getRef().setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    System.out.println("Copy failed");
                                } else {
                                    System.out.println("Success");
                                    queryRefLinhaAtualOld.getRef().removeValue();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Copy failed");
                }
            });
        } else { //Iniciou carro a primeira vez
            queryRefNovaLinha.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, String> mapValues = new HashMap<>();
                    mapValues.put("id", carroId);
                    mapValues.put("latitude", "0");
                    mapValues.put("longitude", "0");
                    mapValues.put("location", "inicial");
                    queryRefNovaLinha.getRef().setValue(mapValues);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public static void atualizaMunicipioAtual(Municipio municipioSelecionado) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        Municipio novoMunicipio = getMunicipios(municipioSelecionado.getId()).get(0);
        Municipio municipioAtualOld = getMunicipioAtual();
        ContentValues values = new ContentValues();
        values.put(SQLiteObjectsHelper.TMunicipioAtual.COLUMN_MUNICIPIOID, novoMunicipio.getId());
        if(municipioAtualOld == null) {
            db.beginTransaction();
            novoMunicipio.setId(db.insert(SQLiteObjectsHelper.TMunicipioAtual.TABLE_NAME, null, values));
        } else {
            StringBuilder whereClause = new StringBuilder();
            whereClause.append(SQLiteObjectsHelper.TMunicipioAtual.COLUMN_MUNICIPIOID).append(" = ?");
            db.beginTransaction();
            db.update(SQLiteObjectsHelper.TMunicipioAtual.TABLE_NAME, values, whereClause.toString(), new String[]{municipioAtualOld.getId().toString()});
        }

        App.setMunicipio(novoMunicipio);

        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
