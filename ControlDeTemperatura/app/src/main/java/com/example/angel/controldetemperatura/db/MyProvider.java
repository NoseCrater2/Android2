package com.example.angel.controldetemperatura.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyProvider extends ContentProvider {

    private static final int MANY = 0;
    private static final int ONE = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DataBase.CONTENT_AUTHORITY,DataBase.PATCH_TEMPS,MANY);
        sUriMatcher.addURI(DataBase.CONTENT_AUTHORITY,DataBase.PATCH_TEMPS+"/#",ONE);
    }

    private MyDbHelper myDbHelper;


    @Override
    public boolean onCreate() {
        myDbHelper = new MyDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = myDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case MANY: cursor = database.query(DataBase.TempsEntry.BD_TABLA,projection,selection,selectionArgs,null,null,sortOrder);

            break;

            case ONE:
                //selection= DataBase.TempsEntry.COLUMN_NO_INVERNADERO+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DataBase.TempsEntry.BD_TABLA,projection,selection,selectionArgs,null,null,sortOrder);

                break;
           default:
               throw new IllegalArgumentException("Unknow URI"+uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }



    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MANY: return insertarTemp(uri,contentValues);
            default:
                throw new  IllegalArgumentException("Datos no validos"+uri);
        }
    }

    private Uri insertarTemp(Uri uri,ContentValues values){
        SQLiteDatabase database = myDbHelper.getWritableDatabase();
        long id = database.insert(DataBase.TempsEntry.BD_TABLA,null,values);
        if(id == -1){
            Toast.makeText(getContext(),"Falló insertar datos",Toast.LENGTH_SHORT).show();
           // Log.e(MyProvider.class.getSimpleName(),"Fallo insertar datos"+uri);
            return null;
        }

        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
