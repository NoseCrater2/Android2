package com.example.angel.mylocalibrtester;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonSendBroadcast;

    LocalBroadcastManager localBroadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSendBroadcast = (Button)findViewById(R.id.buttonSendBroadcast);
        localBroadcastManager=LocalBroadcastManager.getInstance(getApplicationContext());

        buttonSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent();
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setAction("my.custom.action.tag.fordemo");
                localBroadcastManager.sendBroadcast(intent);

            }
        });
    }
}
