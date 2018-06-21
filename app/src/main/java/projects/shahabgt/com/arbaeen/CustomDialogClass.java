package com.example.jonathan.arbaeen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.jonathan.arbaeen.classes.BackgroundTask;

/**
 * Created by Jonathan on 10/10/2017.
 */

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button doa,azkar,nohe,adab;
    Intent intent;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        doa = (Button) findViewById(R.id.custom_doa);
        azkar = (Button) findViewById(R.id.custom_azkar);
        nohe = (Button) findViewById(R.id.custom_nohe);
        adab = (Button) findViewById(R.id.custom_adab);
        doa.setOnClickListener(this);
        azkar.setOnClickListener(this);
        nohe.setOnClickListener(this);
        adab.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_doa:
                intent =new Intent(c,AzkarActivity.class);
                intent.putExtra("where","doa");
                c.startActivity(intent);
                c.finish();
                dismiss();
                break;
            case R.id.custom_azkar:
                intent =new Intent(c,AzkarActivity.class);
                intent.putExtra("where","azkar");
                c.startActivity(intent);
                c.finish();
                dismiss();
                break;
            case R.id.custom_nohe:
                if (checknet()) {
                    BackgroundTask backgroundTask = new BackgroundTask(c, 1);
                    backgroundTask.execute("getnohe");

                } else {
                    Toast.makeText(c,"اتصال به اینترنت را بررسی کنید!",Toast.LENGTH_LONG).show();
                }
//                intent =new Intent(c,AzkarActivity.class);
//                intent.putExtra("where","nohe");
//                c.startActivity(intent);
//                c.finish();
//                dismiss();
                break;
            case R.id.custom_adab:
                intent =new Intent(c,AzkarActivity.class);
                intent.putExtra("where","adab");
                c.startActivity(intent);
                c.finish();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
    private boolean checknet(){
        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            return true;
        }else{
            return false;
        }
    }
}