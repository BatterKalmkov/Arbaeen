package com.example.jonathan.arbaeen;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RekatActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;
    TextView reknum,sejnum;
    LinearLayout reset;
    ImageView plus,minus;
    int sejde=0;
    int rekat=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekat);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        reknum = (TextView)findViewById(R.id.rekat_reknum);
        sejnum = (TextView)findViewById(R.id.rekat_sejnum);
        reset=(LinearLayout)findViewById(R.id.rekat_reset);
        plus=(ImageView)findViewById(R.id.rekat_plus);
        minus=(ImageView)findViewById(R.id.rekat_minus);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sejde<2) {
                    sejde++;
                    sejnum.setText(""+sejde);
                }else {
                    if(rekat<4){
                        sejde=1;
                        sejnum.setText(""+sejde);
                        rekat++;
                        reknum.setText(""+rekat);}
                    else {
                        Toast.makeText(getApplicationContext(), "نماز تمام", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rekat==1){
                    if(sejde>0){
                        sejde--;
                        sejnum.setText(""+sejde);
                    }
                }else {
                    if(sejde>1){
                        sejde--;
                        sejnum.setText(""+sejde);
                    }else {
                        sejde=2;
                        sejnum.setText(""+sejde);
                        rekat--;
                        reknum.setText(""+rekat);
                    }
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rekat=1;
                sejde=0;
                sejnum.setText(""+sejde);
                reknum.setText(""+rekat);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                if(sejde<2) {
                    sejde++;
                    sejnum.setText(""+sejde);
                }else {
                    if(rekat<4){
                        sejde=1;
                        sejnum.setText(""+sejde);
                        rekat++;
                        reknum.setText(""+rekat);}
                    else {
                        Toast.makeText(getApplicationContext(), "نماز تمام", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RekatActivity.this,IntroActivity.class));
        RekatActivity.this.finish();
    }
}
