package com.example.angel.controldetemperatura;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.angel.controldetemperatura.db.DataBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyService extends Service {

    private NotificationManager notificationManager;
    private static final int ID_NOTIFICATION = 1234;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notificacionMenos20();
        notificacionMas20();

        return super.onStartCommand(intent, flags, startId);
    }


    public void notificacionMas20(){
        if(MainActivity.mas20==true){
            long vibrate[] = {0,100,100};
            NotificationCompat.Builder  builder = new NotificationCompat.Builder(
                    getBaseContext())
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("INFO DE TEMPERATURA")
                    .setContentText("20° o más sobre la mínima de la ultima hora")
                    .setVibrate(vibrate)
                    .setWhen(System.currentTimeMillis());

            notificationManager.notify(ID_NOTIFICATION,builder.build());
            MainActivity.mas20 = false;
        }
    }

    public void notificacionMenos20()
    {
        if(MainActivity.menos20==true){
            long vibrate[] = {0,100,100};
            NotificationCompat.Builder  builder = new NotificationCompat.Builder(
                    getBaseContext())
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("INFO DE TEMPERATURA")
                    .setContentText("20° o menos sobre la máxima de la ultima hora")
                    .setVibrate(vibrate)
                    .setWhen(System.currentTimeMillis());

            notificationManager.notify(ID_NOTIFICATION,builder.build());
            MainActivity.menos20 = false;
        }
    }
}
