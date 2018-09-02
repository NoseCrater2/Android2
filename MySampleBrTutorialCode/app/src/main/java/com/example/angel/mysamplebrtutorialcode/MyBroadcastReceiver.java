package com.example.angel.mysamplebrtutorialcode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Broadcast Receiver Triggered", Toast.LENGTH_SHORT).show();
    }
}
