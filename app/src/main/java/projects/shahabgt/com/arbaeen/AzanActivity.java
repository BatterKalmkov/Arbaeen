package com.example.jonathan.arbaeen;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.jonathan.arbaeen.classes.NotificationService;

public class AzanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azan);

    }

    @Override
    public void onBackPressed() {
        try {
            final AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, NotificationService.originalVolume, 0);
            NotificationService.mpintro.stop();
            NotificationService.mpintro.release();
            NotificationService.managerCompat.cancel(1111);
        }catch (Exception e){}
        Intent intent = new Intent(AzanActivity.this,MainActivity.class);
        Intent service = new Intent(AzanActivity.this,NotificationService.class);
        intent.putExtra("where","nowhere");
        stopService(service);
        startActivity(intent);
        this.finish();
    }
}
