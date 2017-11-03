package com.marcosevaristo.trackussource.database;

import android.provider.BaseColumns;

public class SQLiteObjectsHelper {

    public static class TLinhas implements BaseColumns, OperacoesComColunas {
        public static final String TABLE_NAME = "TB_LINHAS";
        public static final String COLUMN_NUMERO = "LIN_NUMERO";
        public static final String COLUMN_TITULO = "LIN_TITULO";
        public static final String COLUMN_SUBTITULO = "LIN_SUBTITULO";
        public static final String COLUMN_CIDADE = "LIN_MUNID";

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
            sb.append(", FOREIGN KEY (").append(COLUMN_CIDADE).append(") REFERENCES ").append(TMunicipioAtual.TABLE_NAME).append("(")
                    .append(TLinhas._ID).append(") ON DELETE CASCADE");
            sb.append(");");
            return sb.toString();
        }

        @Override
        public String getColunasParaSelect() {
            StringBuilder sb = new StringBuilder();
            sb.append("LIN.").append(_ID).append(", ").append(COLUMN_NUMERO).append(", ")
                    .append(COLUMN_TITULO).append(", ").append(COLUMN_SUBTITULO).append(", ").append(COLUMN_CIDADE);
            return sb.toString();
        }
    }

    public static class TLinhaAtual implements BaseColumns, OperacoesComColunas {
        public static final String TABLE_NAME = "TB_LINHA_ATUAL";
        public static final String COLUMN_LINHAID = "LIA_LINID";

        private static TLinhaAtual instance;

        public static TLinhaAtual getInstance() {
            if(instance == null) {
                instance = new TLinhaAtual();
            }
            return instance;
        }

        @Override
        public String getColunasParaSelect() {
            StringBuilder sb = new StringBuilder();
            sb.append("LIA.").append(_ID).append(COLUMN_LINHAID);
            return sb.toString();
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

    public static class TMunicipios implements BaseColumns, OperacoesComColunas {
        public static final String TABLE_NAME = "TB_MUNICIPIOS";
        public static final String COLUMN_MUNNOME = "MUN_MUNNOME";

        private static TMunicipios instance;

        public static TMunicipios getInstance() {
            if(instance == null) {
                instance = new TMunicipios();
            }
            return instance;
        }

        @Override
        public String getColunasParaSelect() {
            StringBuilder sb = new StringBuilder();
            sb.append("MUN.").append(_ID).append(", ").append(", ").append(COLUMN_MUNNOME);
            return sb.toString();
        }

        @Override
        public String getCreateEntry() {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME);
            sb.append(" (").append(_ID).append(" INTEGER NOT NULL PRIMARY KEY ");
            sb.append(", ").append(COLUMN_MUNNOME).append(" VARCHAR(255) NOT NULL ");
            sb.append(");");
            return sb.toString();
        }
    }

    public static class TMunicipioAtual implements BaseColumns, OperacoesComColunas {

        public static final String TABLE_NAME = "TB_MUNICIPIO_ATUAL";
        public static final String COLUMN_MUNICIPIOID = "MUA_MUNID";

        private static TMunicipioAtual instance;

        public static TMunicipioAtual getInstance() {
            if(instance == null) {
                instance = new TMunicipioAtual();
            }
            return instance;
        }

        @Override
        public String getColunasParaSelect() {
            StringBuilder sb = new StringBuilder();
            sb.append("MUN.").append(_ID).append(", ").append(", ").append(COLUMN_MUNICIPIOID);
            return sb.toString();
        }

        @Override
        public String getCreateEntry() {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(TABLE_NAME);
            sb.append(" (").append(_ID).append(" INTEGER NOT NULL PRIMARY KEY ");
            sb.append(", ").append(COLUMN_MUNICIPIOID).append(" INTEGER NOT NULL ");
            sb.append(", FOREIGN KEY (").append(COLUMN_MUNICIPIOID).append(") REFERENCES ").append(TMunicipios.TABLE_NAME).append("(")
                    .append(TLinhas._ID).append(") ON DELETE CASCADE");
            sb.append(");");
            return sb.toString();
        }
    }



}
