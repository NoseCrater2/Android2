package com.example.angel.controldetemperatura;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angel.controldetemperatura.db.DataBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Fragment2 extends Fragment {

    TextView textMax;
    TextView textShowAll;
    Button btnShowAll;
    EditText numEntry;
    Button btnAdd;
    TextView textMinimo;
    TextView txtPromedio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.invernadero_2,container,false);
    }


    private Double tempToEntry = 0.0;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textMax = (TextView)  getActivity().findViewById(R.id.txtMax3);
        textShowAll = (TextView)getActivity().findViewById(R.id.txtTodos2);
        btnShowAll = (Button)getActivity().findViewById(R.id.btnShowTodos3);
        numEntry = (EditText)getActivity().findViewById(R.id.editTemp2);
        btnAdd = (Button)getActivity().findViewById(R.id.btnAgregar2);
        textMinimo = (TextView)getActivity().findViewById(R.id.txtMin2);
        txtPromedio = (TextView)getActivity().findViewById(R.id.txtProm3);

        textMax.setText("Temperatura Máxima: "+getMaxInvIndividual(2));
        textMinimo.setText("Temperatura Minima: "+getMinInvIndividual(2));
        txtPromedio.setText("Temperatura Promedio: "+getPromInvIndividual(2));

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


                double minima = tempToEntry-getMinIndividualPerHour(2);
                double maxima  = getMaxIndividualPerHour(2)-tempToEntry;
                if(minima>=20.0){
                    MainActivity.mas20 = true;
                }else if(maxima>=20.0){
                    MainActivity.menos20 = true;
                }

                //insertarInvernadero1(Double.parseDouble(numEntry.getText().toString()));
                textMax.setText("Temperatura Máxima: "+getMaxInvIndividual(2));
                textMinimo.setText("Temperatura Minima: "+getMinInvIndividual(2));
                txtPromedio.setText("Temperatura Promedio: "+getPromInvIndividual(2));
                detenerServicio();
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
        Cursor cursor = getContext().getContentResolver().query(DataBase.TempsEntry.CONTENT_URI,projection, DataBase.TempsEntry.COLUMN_NO_INVERNADERO+" = 2",null,null);
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

            cadena +="ID:"+currentID+"No Inv: "+noInvernadero+" Temp: "+temperatura+" Hora"+hora+" Dia: "+dia+"\n";

        }
        cursor.close();

        return cadena;
    }

    public void insertarInvernadero1(double temp){

        ContentValues values = new ContentValues();
        values.put(DataBase.TempsEntry.COLUMN_NO_INVERNADERO,2);
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

}
