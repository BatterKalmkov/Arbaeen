package com.example.jonathan.arbaeen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jonathan.arbaeen.classes.MyReceiver;
import com.example.jonathan.arbaeen.classes.NotificationService;
import com.example.jonathan.arbaeen.pedo.StepDetector;
import com.example.jonathan.arbaeen.pedo.StepListener;
import com.example.jonathan.arbaeen.pedo.pedo;

public class PedoActivity extends AppCompatActivity {
    private TextView TvSteps,total;
    MyBroadcastReceiver myReceiver;
    Intent myIntent;
    SharedPreferences sp;
    int steps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedo);
        sp= getApplicationContext().getSharedPreferences("logininfo",0);
        total = (TextView) findViewById(R.id.pedo_totalsteps);
        TvSteps = (TextView) findViewById(R.id.pedo_steps);
        myIntent = new Intent(this, pedo.class);
        total.setText("تعداد کل قدم ها"+sp.getInt("totalsteps",0));
        startService(myIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myReceiver = new MyBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter("YourAction");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }


    @Override
    public void onBackPressed() {
                if(myReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        myReceiver = null;
        stopService(myIntent);
        int ts=sp.getInt("totalsteps",0);
        sp.edit().putInt("totalsteps",ts+steps).commit();
        startActivity(new Intent(PedoActivity.this,IntroActivity.class));
        PedoActivity.this.finish();
    }
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Here you have the received broadcast
            // And if you added extras to the intent get them here too
            // this needs some null checks
            Bundle b = intent.getExtras();
            steps = b.getInt("steps");
            TvSteps.setText(steps+"");
            ///do something with someDouble
        }
    }
}