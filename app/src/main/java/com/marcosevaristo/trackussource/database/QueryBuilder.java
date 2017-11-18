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
import com.marcosevaristo.trackussource.model.Linha;
import com.marcosevaristo.trackussource.model.Municipio;
import com.marcosevaristo.trackussource.utils.CollectionUtils;
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
                linhaAux.setId(cursor.getString(1));
                linhaAux.setNumero(cursor.getString(2));
                linhaAux.setTitulo(cursor.getString(3));
                linhaAux.setSubtitulo(cursor.getString(4));
                linhaAux.setMunicipio(new Municipio(cursor.getLong(5)));
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
                municipioAux.setIdSql(cursor.getLong(0));
                municipioAux.setId(cursor.getString(1));
                municipioAux.setNome(cursor.getString(2));
                if(App.getMunicipio() != null) {
                    municipioAux.setEhMunicipioAtual(App.getMunicipio().getIdSql().equals(municipioAux.getIdSql()));
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

            if(cursor.getCount() > 0) {
                municipioAux = new Municipio();
                municipioAux.setIdSql(cursor.getLong(0));
                municipioAux.setId(cursor.getString(1));
                municipioAux.setNome(cursor.getString(2));
            }

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
            if(cursor.getCount() > 0) {
                linhaAux = new Linha();
                linhaAux.setIdSql(cursor.getLong(0));
                linhaAux.setId(cursor.getString(1));
                linhaAux.setNumero(cursor.getString(2));
                linhaAux.setTitulo(cursor.getString(3));
                linhaAux.setSubtitulo(cursor.getString(4));
                linhaAux.setMunicipio(new Municipio(cursor.getLong(5)));
            }

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
        List<Linha> linhasGravadas = getLinhas(null);
        Map<String, Linha> mLinhasAux = null;
        if (CollectionUtils.isNotEmpty(linhasGravadas)) {
            mLinhasAux = new HashMap<>();
            for (Linha umaLinhaGravada : linhasGravadas) {
                mLinhasAux.put(umaLinhaGravada.getNumero() + "|" + umaLinhaGravada.getMunicipio().getIdSql(), umaLinhaGravada);
            }
        }

        ContentValues values = new ContentValues();
        db.beginTransaction();
        for (Linha umaLinha : lLinhas) {
            if (mLinhasAux != null && mLinhasAux.get(umaLinha.getNumero() + "|" + umaLinha.getMunicipio().getIdSql()) != null) {
                continue;
            }
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_NUMERO, umaLinha.getNumero());
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_IDFIREBASE, umaLinha.getId());
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_TITULO, umaLinha.getTitulo());
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_SUBTITULO, umaLinha.getSubtitulo());
            values.put(SQLiteObjectsHelper.TLinhas.COLUMN_MUNICIPIO, App.getMunicipio().getIdSql());
            umaLinha.setIdSql(db.insert(SQLiteObjectsHelper.TLinhas.TABLE_NAME, null, values));
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void atualizaLinhaAtual(Linha novaLinha) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        Linha linhaAtualAux = getLinhas(novaLinha.getNumero()).get(0);
        Linha linhaAtualOld = getLinhaAtual();
        ContentValues values = new ContentValues();
        values.put(SQLiteObjectsHelper.TLinhaAtual.COLUMN_LINHAID, linhaAtualAux.getIdSql());
        db.beginTransaction();
        if(linhaAtualOld == null) {
            db.insert(SQLiteObjectsHelper.TLinhaAtual.TABLE_NAME, null, values);
        } else {
            StringBuilder whereClause = new StringBuilder();
            whereClause.append(SQLiteObjectsHelper.TLinhaAtual.COLUMN_LINHAID).append(" = ?");
            db.update(SQLiteObjectsHelper.TLinhaAtual.TABLE_NAME, values, whereClause.toString(), new String[]{linhaAtualOld.getIdSql().toString()});
        }
        alteraLinhaAtualFirebase(novaLinha);
        App.setLinhaAtual(linhaAtualAux);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private static void alteraLinhaAtualFirebase(Linha novaLinha) {
        if(App.getLinhaAtual() != null) {
            queryRefLinhaAtualOld = FirebaseUtils.getCarroReference(App.getLinhaAtual().getId(), App.getCarroId());
        }

        queryRefNovaLinha = FirebaseUtils.getCarroReference(novaLinha.getId(), App.getCarroId());
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
                    mapValues.put("id", App.getCarroId());
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
        Municipio novoMunicipio = getMunicipios(municipioSelecionado.getIdSql()).get(0);
        Municipio municipioAtualOld = getMunicipioAtual();
        ContentValues values = new ContentValues();
        values.put(SQLiteObjectsHelper.TMunicipioAtual.COLUMN_MUNICIPIOID, novoMunicipio.getIdSql());
        if(municipioAtualOld == null) {
            db.beginTransaction();
            db.insert(SQLiteObjectsHelper.TMunicipioAtual.TABLE_NAME, null, values);
        } else {
            StringBuilder whereClause = new StringBuilder();
            whereClause.append(SQLiteObjectsHelper.TMunicipioAtual.COLUMN_MUNICIPIOID).append(" = ?");
            db.beginTransaction();
            db.update(SQLiteObjectsHelper.TMunicipioAtual.TABLE_NAME, values, whereClause.toString(),
                    new String[]{municipioAtualOld.getIdSql().toString()});
        }

        App.setMunicipio(novoMunicipio);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void insereMunicipios(List<Municipio> lMunicipiosAux) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        List<Municipio> municipiosGravados = getMunicipios(null);
        Map<String, Municipio> mMunicipiosAux = null;
        if(CollectionUtils.isNotEmpty(municipiosGravados)) {
            mMunicipiosAux = new HashMap<>();
            for(Municipio umMunicipioGravado : municipiosGravados) {
                mMunicipiosAux.put(umMunicipioGravado.getIdSql()+"|"+umMunicipioGravado.getNome(), umMunicipioGravado);
            }
        }

        db.beginTransaction();
        for (Municipio umMunicipio : lMunicipiosAux) {
            if(mMunicipiosAux != null && mMunicipiosAux.get(umMunicipio.getIdSql()+"|"+umMunicipio.getNome()) != null) {
                continue;
            }
            values.put(SQLiteObjectsHelper.TMunicipios._ID, umMunicipio.getIdSql());
            values.put(SQLiteObjectsHelper.TMunicipios.COLUMN_IDFIREBASE, umMunicipio.getId());
            values.put(SQLiteObjectsHelper.TMunicipios.COLUMN_MUNNOME, umMunicipio.getNome());
            umMunicipio.setIdSql(db.insert(SQLiteObjectsHelper.TMunicipios.TABLE_NAME, null, values));
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
