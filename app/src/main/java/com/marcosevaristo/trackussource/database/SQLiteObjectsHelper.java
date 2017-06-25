package com.marcosevaristo.trackussource.database;

import android.provider.BaseColumns;

public class SQLiteObjectsHelper implements OperacoesComColunas{
    private static SQLiteObjectsHelper instance;

    private SQLiteObjectsHelper(){}

    public static SQLiteObjectsHelper getInstance() {
        if(instance == null) {
            instance = new SQLiteObjectsHelper();
        }
        return instance;
    }

    @Override
    public String getColunasParaSelect() {
        return null;
    }

    @Override
    public String getCreateEntry() {
        StringBuilder sb = new StringBuilder();
        sb.append(TLinhas.getInstance().getCreateEntry());
        sb.append(TLinhaAtual.getInstance().getCreateEntry());
        return sb.toString();
    }


    public static class TLinhas implements BaseColumns, OperacoesComColunas {
        public static String TABLE_NAME = "TB_LINHAS";
        public static String COLUMN_NUMERO = "LIN_NUMERO";
        public static String COLUMN_TITULO = "LIN_TITULO";
        public static String COLUMN_SUBTITULO = "LIN_SUBTITULO";
        public static String COLUMN_CIDADE = "LIN_CIDADEID";
        public static String COLUMN_LINHAATUAL = "LIN_LINHAATUAL";

        private static TLinhas instance;

        public static TLinhas getInstance() {
            if(instance == null) {
                instance = new TLinhas();
            }
            return instance;
        }

        @Override
        public String getCreateEntry(){
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME);
            sb.append(" (").append(_ID).append(" INTEGER NOT NULL PRIMARY KEY ");
            sb.append(",").append(COLUMN_NUMERO).append(" VARCHAR(25) NOT NULL ");
            sb.append(",").append(COLUMN_TITULO).append(" VARCHAR(255) NOT NULL ");
            sb.append(",").append(COLUMN_SUBTITULO).append(" VARCHAR(600) NULL ");
            sb.append(",").append(COLUMN_CIDADE).append(" INTEGER NULL ");
            sb.append(",").append(COLUMN_LINHAATUAL).append(" INTEGER NOT NULL ");
            sb.append(");");
            return sb.toString();
        }

        @Override
        public String getColunasParaSelect() {
            StringBuilder sb = new StringBuilder();
            sb.append(COLUMN_NUMERO).append(", ").append(COLUMN_TITULO).append(", ").append(COLUMN_SUBTITULO)
                    .append(", ").append(COLUMN_CIDADE).append(", ").append(COLUMN_LINHAATUAL);
            return sb.toString();
        }
    }

    public static class TLinhaAtual implements BaseColumns, OperacoesComColunas {
        public static String TABLE_NAME = "TB_LINHA_ATUAL";
        public static String COLUMN_LINHAID = "LIA_LINID";

        private static TLinhaAtual instance;

        public static TLinhaAtual getInstance() {
            if(instance == null) {
                instance = new TLinhaAtual();
            }
            return instance;
        }

        @Override
        public String getColunasParaSelect() {
            return COLUMN_LINHAID;
        }

        @Override
        public String getCreateEntry() {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME);
            sb.append(" (").append(_ID).append(" INTEGER NOT NULL PRIMARY KEY ");
            sb.append(",").append(COLUMN_LINHAID).append(" INTEGER NOT NULL ");
            sb.append(", FOREIGN KEY (").append(COLUMN_LINHAID).append(") REFERENCES ").append(TLinhas.TABLE_NAME).append("(")
                    .append(TLinhas._ID).append(") ON DELETE CASCADE");
            sb.append(");");
            return sb.toString();
        }
    }



}
