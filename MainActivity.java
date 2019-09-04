package com.faisal.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startMailCalendarService(this);
        handleOpenApp();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (LoadService.isServiceRunning(LoadService.class, getApplicationContext())) {
            Intent service = new Intent(getApplicationContext(), LoadService.class);
            stopService(service);
        }
    }
    private synchronized void startMailCalendarService(Context context){
        try {
            if (LoadService.isServiceRunning(LoadService.class, getApplicationContext())) {
                Intent service = new Intent(context, LoadService.class);
                context.startService(service);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void handleOpenApp() {
        try {
            if (LoadService.isServiceRunning(LoadService.class, getApplicationContext())) {
                startActivity(new Intent(this, MainActivity.class).putExtra(LoadService.ACTION, LoadService.ACTION_LOAD));
            }else {
                startMailCalendarService(getApplicationContext());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }
}
