package com.marcosevaristo.trackussource.database;

import android.provider.BaseColumns;

public class DatabaseHelper {

    public static class SQLiteObjectsHelper {
        private SQLiteObjectsHelper(){}

        public static class TLinhas implements BaseColumns {
            public static String TABLE_NAME = "TB_LINHAS";
            public static String COLUMN_NUMERO = "LIN_NUMERO";
            public static String COLUMN_TITULO = "LIN_TITULO";
            public static String COLUMN_SUBTITULO = "LIN_SUBTITULO";
            public static String COLUMN_CIDADE = "LIN_CIDADEID";

            public static String getCreateEntry(){
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE ").append(TABLE_NAME);
                sb.append(" (").append(_ID).append(" INTEGER NOT NULL PRIMARY KEY ");
                sb.append(",").append(COLUMN_NUMERO).append(" VARCHAR(25) NOT NULL ");
                sb.append(",").append(COLUMN_TITULO).append(" VARCHAR(255) NOT NULL ");
                sb.append(",").append(COLUMN_SUBTITULO).append(" VARCHAR(600) NULL ");
                sb.append(",").append(COLUMN_CIDADE).append(" INTEGER NULL ");
                sb.append(")");
                return sb.toString();
            }

            public static String getColunasParaSelect() {
                StringBuilder sb = new StringBuilder();
                sb.append(COLUMN_NUMERO).append(", ").append(COLUMN_TITULO).append(", ").append(COLUMN_SUBTITULO).append(", ").append(COLUMN_CIDADE);
                return sb.toString();
            }
        }
    }

}
