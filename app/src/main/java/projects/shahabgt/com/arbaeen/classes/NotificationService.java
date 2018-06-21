package com.example.jonathan.arbaeen.classes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.example.jonathan.arbaeen.AzanActivity;
import com.example.jonathan.arbaeen.R;

import static android.support.v4.content.WakefulBroadcastReceiver.completeWakefulIntent;

/**
 * Created by Jonathan on 9/2/2017.
 */


public class NotificationService extends Service {

    NotificationManager mManager;
    public static MediaPlayer mpintro;
    public static NotificationManagerCompat managerCompat;
    public static int originalVolume;
    AudioManager mAudioManager;

    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        mpintro = MediaPlayer.create(this, R.raw.azan);
        mpintro.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mpintro.setLooping(false);
        String Noti_title = intent.getExtras().getString("title");
        String Noti_message = intent.getExtras().getString("notes");
        int Noti_Code = intent.getExtras().getInt("code");

        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), AzanActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent deleteintent = new Intent(this, DeletenotiReceiver.class);
        PendingIntent deletependingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, deleteintent, 0);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Noti_title)
                .setContentText(Noti_message)
               // .setOngoing(true)
                .setContentIntent(pendingNotificationIntent)
                .setDeleteIntent(deletependingIntent)
                .setVibrate(new long[]{100L, 100L, 200L, 500L});

        Notification notification = builder.build();
        managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(Noti_Code, notification);
        mpintro.start();
        return START_NOT_STICKY;
    }


}