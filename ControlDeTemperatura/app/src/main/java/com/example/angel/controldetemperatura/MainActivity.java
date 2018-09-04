package com.example.angel.controldetemperatura;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angel.controldetemperatura.db.DataBase;
import com.example.angel.controldetemperatura.db.MyDbHelper;
import com.example.angel.controldetemperatura.file.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button button;
    ListView list;
    String [] invernaderos = {"Invernadero 1","Invernadero 2","Invernadero 3"};
    TextView textView;
    public static boolean mas20 = false;
    public static boolean menos20 = false;
    public static boolean promedio10 = false;


    private final  int REQUES_ACCESS_FINE = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.export_scv);
        list= (ListView)findViewById(R.id.inv_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,invernaderos);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

       // textView = (TextView)findViewById(R.id.textFirst);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

               permiso();

            }
        });
    }

    public void permiso(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUES_ACCESS_FINE);
        }else
            exportDB();
        Toast.makeText(this, "bdcompleta.csv guardada en raiz de memoria externa ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUES_ACCESS_FINE){
            if( grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                exportDB();
                Toast.makeText(this, "bdcompleta.csv guardada en raiz de memoria externa ", Toast.LENGTH_SHORT).show();
            }

            else
                Toast.makeText(this,"Permiso denegado",Toast.LENGTH_SHORT).show();
        }
    }

    private void addFragment1(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Fragment1 sampleFragment=new Fragment1();
        fragmentTransaction.add(R.id.fragmentContainer,sampleFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addFragment2(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Fragment2 sampleFragment=new Fragment2();
        fragmentTransaction.add(R.id.fragmentContainer,sampleFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void addFragment3(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Fragment3 sampleFragment=new Fragment3();
        fragmentTransaction.add(R.id.fragmentContainer,sampleFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String inv =  list.getItemAtPosition(i).toString();
        String numInvernadero =inv.substring(inv.length()-1,inv.length());
        switch (numInvernadero){
            case "1":
                addFragment1();

                break;
            case "2":
                addFragment2();
                break;
            case "3":
                addFragment3();

                break;
        }
    }


    private String obtenerFecha(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fechaActual = format.format(cal.getTime());
        return fechaActual;
    }

    private String obtenerHora(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        String fechaActual = format.format(cal.getTime());
        return fechaActual;
    }



    public void insertarInvernadero2(){

        ContentValues values = new ContentValues();
        values.put(DataBase.TempsEntry.COLUMN_NO_INVERNADERO,2);
        values.put(DataBase.TempsEntry.COLUMN_TEMP,24.0);
        values.put(DataBase.TempsEntry.COLUMN_DIA,obtenerFecha());
        values.put(DataBase.TempsEntry.COLUMN_HORA,obtenerHora());

        Uri newUri = getContentResolver().insert(DataBase.TempsEntry.CONTENT_URI,values);

    }

    public void insertarInvernadero3(){

        ContentValues values = new ContentValues();
        values.put(DataBase.TempsEntry.COLUMN_NO_INVERNADERO,3);
        values.put(DataBase.TempsEntry.COLUMN_TEMP,24.0);
        values.put(DataBase.TempsEntry.COLUMN_DIA,obtenerFecha());
        values.put(DataBase.TempsEntry.COLUMN_HORA,obtenerHora());

        Uri newUri = getContentResolver().insert(DataBase.TempsEntry.CONTENT_URI,values);

    }

    String cadena;
    private String mostrarTodaInfo(){
        cadena = "";
        String[] projection = {DataBase.TempsEntry._ID,
                DataBase.TempsEntry.COLUMN_NO_INVERNADERO,
                DataBase.TempsEntry.COLUMN_TEMP,
                DataBase.TempsEntry.COLUMN_HORA,
                DataBase.TempsEntry.COLUMN_DIA};
        Cursor cursor = getContentResolver().query(DataBase.TempsEntry.CONTENT_URI,projection, null,null,null);
        int idColumnIndex = cursor.getColumnIndex(DataBase.TempsEntry._ID);
        int invColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_NO_INVERNADERO);
        int tempColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_TEMP);
        int horaColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_HORA);
        int diaColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_DIA);
        while (cursor.moveToNext()){
            int currentID = cursor.getInt(idColumnIndex);
            String noInvernadero = cursor.getString(invColumIndex);
            String temperatura = cursor.getString(tempColumIndex);
            String hora = cursor.getString(horaColumIndex);
            String dia = cursor.getString(diaColumIndex);

            cadena +="ID: "+currentID+" INV: "+noInvernadero+" TEMP: "+temperatura+" HORA: "+hora+" DIA: "+dia+"\n";

        }
        cursor.close();

        return cadena;
    }



    private String mostrarTodaInfoInv2(){
        cadena = "";
        String[] projection = {DataBase.TempsEntry._ID,
                DataBase.TempsEntry.COLUMN_NO_INVERNADERO,
                DataBase.TempsEntry.COLUMN_TEMP,
                DataBase.TempsEntry.COLUMN_HORA,
                DataBase.TempsEntry.COLUMN_DIA};
        Cursor cursor = getContentResolver().query(DataBase.TempsEntry.CONTENT_URI,projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+" = 2",null,null);
        int idColumnIndex = cursor.getColumnIndex(DataBase.TempsEntry._ID);
        int invColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_NO_INVERNADERO);
        int tempColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_TEMP);
        int horaColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_HORA);
        int diaColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_DIA);
        while (cursor.moveToNext()){
            int currentID = cursor.getInt(idColumnIndex);
            String noInvernadero = cursor.getString(invColumIndex);
            String temperatura = cursor.getString(tempColumIndex);
            String hora = cursor.getString(horaColumIndex);
            String dia = cursor.getString(diaColumIndex);

            cadena +="- id:"+currentID+" no invernadero: "+noInvernadero+" temp: "+temperatura+" hora"+hora+" dia: "+dia+"\n";

        }
        cursor.close();

        return cadena;
    }

    private String mostrarTodaInfoInv3(){

        cadena = "";
        String[] projection = {DataBase.TempsEntry._ID,
                DataBase.TempsEntry.COLUMN_NO_INVERNADERO,
                DataBase.TempsEntry.COLUMN_TEMP,
                DataBase.TempsEntry.COLUMN_HORA,
                DataBase.TempsEntry.COLUMN_DIA};
        Cursor cursor = getContentResolver().query(DataBase.TempsEntry.CONTENT_URI,projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+" = 3",null,null);
        int idColumnIndex = cursor.getColumnIndex(DataBase.TempsEntry._ID);
        int invColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_NO_INVERNADERO);
        int tempColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_TEMP);
        int horaColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_HORA);
        int diaColumIndex = cursor.getColumnIndex(DataBase.TempsEntry.COLUMN_DIA);
        while (cursor.moveToNext()){
            int currentID = cursor.getInt(idColumnIndex);
            String noInvernadero = cursor.getString(invColumIndex);
            String temperatura = cursor.getString(tempColumIndex);
            String hora = cursor.getString(horaColumIndex);
            String dia = cursor.getString(diaColumIndex);

            cadena +="- id:"+currentID+" no invernadero: "+noInvernadero+" temp: "+temperatura+" hora"+hora+" dia: "+dia+"\n";

        }
        cursor.close();

        return cadena;
    }

    private int obtenerMaximo(){
        String[] projection = {"max(temperatura)"};
        Cursor cursor = getContentResolver().query(DataBase.TempsEntry.CONTENT_URI,null,"1",null,null);
        int tempColumIndex = cursor.getColumnIndex("max(temperatura)");
        int temperatura = cursor.getInt(tempColumIndex);
        return temperatura ;
    }


    public String getMaxInvIndividual(int invernadero ) {
        // use the data type of the column database.open();
        String [] projection = {"MAX(" + DataBase.TempsEntry.COLUMN_TEMP + ") AS MAX"};
        Cursor cursor = getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+"= "+invernadero, null, null);
        cursor.moveToFirst();
        // to move the cursor to first record
         int index = cursor.getColumnIndex("MAX");
         String data = cursor.getString(index);
        // use the data type of the column or use String itself you can parse it database.close();
         return data;
        // }
    }

    public String getMaxTodos() {
        // use the data type of the column database.open();
        String [] projection = {"MAX(" + DataBase.TempsEntry.COLUMN_TEMP + ") AS MAX"};
        Cursor cursor = getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, null, null, null);
        cursor.moveToFirst();
        // to move the cursor to first record
        int index = cursor.getColumnIndex("MAX");
        String data = cursor.getString(index);
        // use the data type of the column or use String itself you can parse it database.close();
        return data;
        // }
    }

    public String getMinInvIndividual(int invernadero ) {
        // use the data type of the column database.open();
        String [] projection = {"MIN(" + DataBase.TempsEntry.COLUMN_TEMP + ") AS MIN"};
        Cursor cursor = getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+"= "+invernadero, null, null);
        cursor.moveToFirst();
        // to move the cursor to first record
        int index = cursor.getColumnIndex("MIN");
        String data = cursor.getString(index);
        // use the data type of the column or use String itself you can parse it database.close();
        return data;
        // }
    }

    public String getMinInvTodos() {
        // use the data type of the column database.open();
        String [] projection = {"MIN(" + DataBase.TempsEntry.COLUMN_TEMP + ") AS MIN"};
        Cursor cursor = getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, null, null, null);
        cursor.moveToFirst();
        // to move the cursor to first record
        int index = cursor.getColumnIndex("MIN");
        String data = cursor.getString(index);
        // use the data type of the column or use String itself you can parse it database.close();
        return data;
        // }
    }

    public String getPromInvIndividual(int invernadero ) {
        // use the data type of the column database.open();
        String [] projection = {"AVG(" + DataBase.TempsEntry.COLUMN_TEMP + ") AS AVG"};
        Cursor cursor = getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+"= "+invernadero, null, null);
        cursor.moveToFirst();
        // to move the cursor to first record
        int index = cursor.getColumnIndex("AVG");
        String data = cursor.getString(index);
        // use the data type of the column or use String itself you can parse it database.close();
        return data;
        // }
    }

    public String getPromTodos() {
        // use the data type of the column database.open();
        String [] projection = {"AVG(" + DataBase.TempsEntry.COLUMN_TEMP + ") AS AVG"};
        Cursor cursor = getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, null, null, null);
        cursor.moveToFirst();
        // to move the cursor to first record
        int index = cursor.getColumnIndex("AVG");
        String data = cursor.getString(index);
        // use the data type of the column or use String itself you can parse it database.close();
        return data;
        // }
    }





    private void exportDB() {

        MyDbHelper dbhelper = new MyDbHelper(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "bdcompleta.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM "+ DataBase.TempsEntry.BD_TABLA,null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2),curCSV.getString(3),curCSV.getString(4)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }




}