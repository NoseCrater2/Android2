package com.example.angel.controldetemperatura;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class Fragment1 extends Fragment {

    ListView lista;

    TextView textMax;
    TextView textShowAll;
    Button btnShowAll;
    EditText numEntry;
    Button btnAdd;
    TextView textMinimo;
    TextView txtPromedio;
    Button btn_exportar;





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.invernadero1_view,container,false);
    }


    Double tempToEntry =0.0;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textMax = (TextView)  getActivity().findViewById(R.id.txtMax3);
        textShowAll = (TextView)getActivity().findViewById(R.id.txtTodos);
        btnShowAll = (Button)getActivity().findViewById(R.id.btnShowTodos3);
        numEntry = (EditText)getActivity().findViewById(R.id.editTemp);
        btnAdd = (Button)getActivity().findViewById(R.id.btnAgregar);
        textMinimo = (TextView)getActivity().findViewById(R.id.txtMin);
        txtPromedio = (TextView)getActivity().findViewById(R.id.txtProm3);
        //btn_exportar = (Button) getActivity().findViewById(R.id.btn_export);

        textMax.setText("Temperatura Máxima: "+getMaxInvIndividual(1));
        textMinimo.setText("Temperatura Minima: "+getMinInvIndividual(1));
        txtPromedio.setText("Temperatura Promedio: "+getPromInvIndividual(1));


        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textShowAll.setText(mostrarTodaInfoInv1());
            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarServicio();

                tempToEntry = Double.parseDouble(numEntry.getText().toString());
                insertarInvernadero1(tempToEntry);


                double minima = tempToEntry-getMinIndividualPerHour(1);
                double maxima  = getMaxIndividualPerHour(1)-tempToEntry;
                if(minima>=20.0){
                    MainActivity.mas20 = true;
                }else if(maxima>=20.0){
                    MainActivity.menos20 = true;
                }

                textMax.setText("Temperatura Máxima: "+getMaxInvIndividual(1));
                textMinimo.setText("Temperatura Minima: "+getMinInvIndividual(1));
                txtPromedio.setText("Temperatura Promedio: "+getPromInvIndividual(1));

                detenerServicio();
            }
        });


        btn_exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDB();

            }
        });

    }


    public void iniciarServicio(){
        Intent intent = new Intent(getActivity(),MyService.class);
        getActivity().startService(intent);
    }

    public void detenerServicio(){
        Intent intent = new Intent(getActivity(),MyService.class);
        getActivity().stopService(intent);
    }

    public String getMaxInvIndividual(int invernadero ) {

        // use the data type of the column database.open();
        String [] projection = {"MAX(" + DataBase.TempsEntry.COLUMN_TEMP + ") AS MAX"};
        Cursor cursor = getContext().getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+"= "+invernadero, null, null);
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
        Cursor cursor = getContext().getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+"= "+invernadero, null, null);
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
        Cursor cursor = getContext().getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+"= "+invernadero, null, null);
        cursor.moveToFirst();
        // to move the cursor to first record
        int index = cursor.getColumnIndex("AVG");
        String data = cursor.getString(index);
        // use the data type of the column or use String itself you can parse it database.close();
        return data;
        // }
    }

    @Override
    public void onDestroy() {
            super.onDestroy();
    }


    String cadena;
    private String mostrarTodaInfoInv1(){
        cadena = "";
        String[] projection = {DataBase.TempsEntry._ID,
                DataBase.TempsEntry.COLUMN_NO_INVERNADERO,
                DataBase.TempsEntry.COLUMN_TEMP,
                DataBase.TempsEntry.COLUMN_HORA,
                DataBase.TempsEntry.COLUMN_DIA};
        Cursor cursor = getContext().getContentResolver().query(DataBase.TempsEntry.CONTENT_URI,projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+" = 1",null,null);
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

            cadena +="ID:"+currentID+" No Inv: "+noInvernadero+" Temp: "+temperatura+" Hora"+hora+" Dia: "+dia+"\n";

        }
        cursor.close();

        return cadena;
    }

    public void insertarInvernadero1(double temp){

        ContentValues values = new ContentValues();
        values.put(DataBase.TempsEntry.COLUMN_NO_INVERNADERO,1);
        values.put(DataBase.TempsEntry.COLUMN_TEMP,temp);
        values.put(DataBase.TempsEntry.COLUMN_DIA,obtenerFecha());
        values.put(DataBase.TempsEntry.COLUMN_HORA,obtenerHora());

        Uri newUri = getContext().getContentResolver().insert(DataBase.TempsEntry.CONTENT_URI,values);


    }


    public int getMinIndividualPerHour(int invernadero) {
        // use the data type of the column database.open();
        String [] projection = {"MIN(" + DataBase.TempsEntry.COLUMN_TEMP + ") AS MIN"};
        Cursor cursor = getContext().getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+"= "+invernadero
                +" and "+DataBase.TempsEntry.COLUMN_DIA+" = '"+obtenerFecha() +"' and "+DataBase.TempsEntry.COLUMN_HORA+" between '"+obtenerHoraAntes()+"' and '"+obtenerHora()+"'" , null, null);
        cursor.moveToFirst();
        // to move the cursor to first record
        int index = cursor.getColumnIndex("MIN");
        int data = cursor.getInt(index);
        // use the data type of the column or use String itself you can parse it database.close();
        return data;
        // }
    }


    public int getMaxIndividualPerHour(int invernadero) {
        // use the data type of the column database.open();
        String [] projection = {"MAX(" + DataBase.TempsEntry.COLUMN_TEMP + ") AS MAX"};
        Cursor cursor = getContext().getContentResolver().query(DataBase.TempsEntry.CONTENT_URI, projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+"= "+invernadero
                +" and "+DataBase.TempsEntry.COLUMN_DIA+" = '"+obtenerFecha() +"' and "+DataBase.TempsEntry.COLUMN_HORA+" between '"+obtenerHoraAntes()+"' and '"+obtenerHora()+"'" , null, null);
        cursor.moveToFirst();
        // to move the cursor to first record
        int index = cursor.getColumnIndex("MAX");
        int data = cursor.getInt(index);
        // use the data type of the column or use String itself you can parse it database.close();
        return data;
        // }
    }



    private String obtenerHoraAntes(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cal.getTime());
        cal.add(Calendar.HOUR,-1);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

        String fechaActual = format.format(cal.getTime());
        return fechaActual;
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

    private void exportDB() {

        MyDbHelper dbhelper = new MyDbHelper(getContext().getApplicationContext());
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
            Cursor curCSV = db.rawQuery("SELECT * FROM "+ DataBase.TempsEntry.BD_TABLA+" WHERE "+ DataBase.TempsEntry.COLUMN_NO_INVERNADERO +" = 1",null);
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
