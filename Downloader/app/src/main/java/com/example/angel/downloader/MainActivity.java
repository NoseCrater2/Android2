package com.example.angel.downloader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int WRITE_REQUEST_CODE = 300;
    private static String TAG = MainActivity.class.getCanonicalName();
    private String url;
    private EditText editTextUrl;
    private final  int REQUES_ACCESS_FINE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUrl = findViewById(R.id.editTextUrl);
        Button downloadButton = findViewById(R.id.buttonDownload);


        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    url = editTextUrl.getText().toString();
                    new DownloadFile().execute(url);
            }
        });

    }


    private class DownloadFile extends AsyncTask<String,String,String>{

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(MainActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // Obtener tamaño del archivo
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Obtener el nombre del archivo a partir de la url
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //agregar la fecha de descarga al nombre del archivo
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "MisDescargas/";

                //Crear la carpeta contenedora si no existe
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Escribir el archivo
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    //Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // escribiendo  datos en el  archivo
                    output.write(data, 0, count);
                }

                // vacíar la salida
                output.flush();

                // cierre de streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;
            } catch (Exception e) {
                Log.e("Error",e.getLocalizedMessage());
               // Toast.makeText(MainActivity.this,"algo salio mal",Toast.LENGTH_LONG).show();
            }
            return "Algo salió mal :/";

        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            this.progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG);
        }
    }


}



