package com.example.jonathan.arbaeen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RosaryActivity extends AppCompatActivity {
    ImageView min,plus;
    LinearLayout reset;
    TextView num;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rosary);
        min=(ImageView)findViewById(R.id.rosary_minus);
        plus=(ImageView)findViewById(R.id.rosary_plus);
        reset=(LinearLayout)findViewById(R.id.rosary_reset);
        num=(TextView)findViewById(R.id.rosary_num);

        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count!=0){
                    count--;
                    num.setText(count+"");
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                num.setText(count+"");
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=0;
                num.setText(count+"");
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RosaryActivity.this,IntroActivity.class));
        RosaryActivity.this.finish();
    }
}
