package com.example.jonathan.arbaeen.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import com.example.jonathan.arbaeen.MainActivity;

import static android.content.Context.AUDIO_SERVICE;


public class DeletenotiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            final AudioManager mAudioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, NotificationService.originalVolume, 0);
            NotificationService.mpintro.stop();
            NotificationService.mpintro.release();
            NotificationService.managerCompat.cancel(1111);
        }catch (Exception e){}
        Intent activity = new Intent(context,MainActivity.class);
        Intent service = new Intent(context,NotificationService.class);
        activity.putExtra("where","nowhere");
        context.stopService(service);
        context.startActivity(activity);
    }

}