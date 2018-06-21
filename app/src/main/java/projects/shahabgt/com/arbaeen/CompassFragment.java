package com.example.jonathan.arbaeen;


import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CompassFragment extends Fragment implements SensorEventListener {
    private TextView countryName , Quibladegree ;
    private RelativeLayout compass ;
    private ImageView indicator ;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        init(view);
        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void init(View view) {
SharedPreferences sp;
        sp= getActivity().getSharedPreferences("location",0);
        int quibla = sp.getInt("quibla",0);
        countryName = (TextView) view.findViewById(R.id.textView11);
        Quibladegree = (TextView) view.findViewById(R.id.textView12);
      //  Quibladegree.setText("Qibla direction from North: "+ quibla);
        indicator = (ImageView) view.findViewById(R.id.imageView2);
        compass = (RelativeLayout) view.findViewById(R.id.compassContainer);
        mSensorManager = (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);
        RotateAnimation ra = new RotateAnimation(currentDegree, quibla,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(400);
        ra.setFillAfter(true);
        indicator.startAnimation(ra);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float degree = Math.round(event.values[0]);

        RotateAnimation ra = new RotateAnimation(currentDegree,
                -degree, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(400);
        ra.setFillAfter(true);
        compass.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
