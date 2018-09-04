package com.example.angel.controldetemperatura.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBase {

    public static final String CONTENT_AUTHORITY = "com.example.angel.controldetemperatura";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATCH_TEMPS  = "base_datosdb";




    public static final class TempsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATCH_TEMPS);
        public static final String BD_TABLA = "tabla_temps";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NO_INVERNADERO = "no_invernadero";
        public static final String COLUMN_TEMP = "temperatura";
        public static final String COLUMN_HORA = "hora";
        public static final String COLUMN_DIA = "dia";
    }
















}
