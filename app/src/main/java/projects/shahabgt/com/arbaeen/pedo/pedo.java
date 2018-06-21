package com.example.jonathan.arbaeen.pedo;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

/**
 * Created by Jonathan on 10/7/2017.
 */

public class pedo extends Service implements SensorEventListener, StepListener  {
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "تعداد قدم ها: ";
    private int numSteps=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
       // TvSteps = (TextView) intent.findViewById(R.id.pedo_steps);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        return START_NOT_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void step(long timeNs) {
            numSteps++;
            Intent intent = new Intent("YourAction");
            intent.putExtra("steps",numSteps);
            //Bundle bundle = new Bundle();
            //bundle.putInt("steps", numSteps);
            //intent.putExtras(intent);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }
}
