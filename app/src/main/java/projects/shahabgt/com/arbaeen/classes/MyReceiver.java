package com.example.jonathan.arbaeen.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.jonathan.arbaeen.TodayFragment;

/**
 * Created by Jonathan on 9/2/2017.
 */

public class MyReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String Noti_title = intent.getExtras().getString("title");
        String Noti_message = intent.getExtras().getString("notes");
        int Noti_code = intent.getExtras().getInt("code");
        Intent myIntent = new Intent(context, NotificationService.class);
        myIntent.putExtra("title", Noti_title);
        myIntent.putExtra("notes", Noti_message);
        myIntent.putExtra("code", Noti_code);
        context.startService(myIntent);
    }
}
