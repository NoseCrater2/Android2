package com.example.angel.controldetemperatura.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DB_NOMBRE = "base_datos.db";
    private static final int DATABASE_VERSION = 1;

    public MyDbHelper(Context context){
        super(context,DB_NOMBRE,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREAR_TABLA = "CREATE TABLE "+DataBase.TempsEntry.BD_TABLA+"("+DataBase.TempsEntry.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +DataBase.TempsEntry.COLUMN_NO_INVERNADERO+" INT NOT NULL, "
                +DataBase.TempsEntry.COLUMN_TEMP+" DOUBLE NOT NULL, "
                +DataBase.TempsEntry.COLUMN_HORA+" TIME NOT NULL, "
                +DataBase.TempsEntry.COLUMN_DIA+" DATE NOT NULL)";

        sqLiteDatabase.execSQL(CREAR_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }





}
