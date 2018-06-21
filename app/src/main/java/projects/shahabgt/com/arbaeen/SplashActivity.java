package com.example.jonathan.arbaeen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.jonathan.arbaeen.adapter.CityModel;
import com.example.jonathan.arbaeen.database.DatabaseOperations;
import com.example.jonathan.arbaeen.database.DatabaseOperationsAdab;
import com.example.jonathan.arbaeen.database.DatabaseOperationsDoa;
import com.example.jonathan.arbaeen.database.DatabaseOperationsNohe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sp;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        sp= SplashActivity.this.getSharedPreferences("logininfo",0);
        String first = sp.getString("first","yes");
        if(first.equals("yes")){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("لطفا منتظر بمانید...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            insert();
            insertdoa();
         //   insertnohe();
            insertadab();
            sp.edit().putString("first","no").apply();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("where", "nowhere");
            progressDialog.dismiss();
            startActivity(intent);
            SplashActivity.this.finish();
        }else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("where", "nowhere");
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, 1500);
        }
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("zekr.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void insert(){
        try {
            DatabaseOperations db = new DatabaseOperations(getApplicationContext());
            JSONArray jsonarray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String name = jsonobject.getString("name");
                String count = jsonobject.getString("count");
                String text = jsonobject.getString("text");
                db.insert(name,count,text);
            }
            db.close();
        } catch (Exception e) {
            Log.d("error",e.getMessage());
        }
    }
    public String loadJSONFromAssetdoa() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("doa.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void insertdoa(){
        try {
            DatabaseOperationsDoa db = new DatabaseOperationsDoa(getApplicationContext());
            JSONArray jsonarray = new JSONArray(loadJSONFromAssetdoa());
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");
                String name = jsonobject.getString("name");
                String text = jsonobject.getString("text");
                db.insert(id,name,text);
            }
            db.close();

        } catch (Exception e) {
            Log.d("error",e.getMessage());
        }
    }
//    public String loadJSONFromAssetnohe() {
//        String json = null;
//        try {
//            InputStream is = this.getAssets().open("nohe.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }
//    private void insertnohe(){
//        try {
//            DatabaseOperationsNohe db = new DatabaseOperationsNohe(getApplicationContext());
//            JSONArray jsonarray = new JSONArray(loadJSONFromAssetnohe());
//            for (int i = 0; i < jsonarray.length(); i++) {
//                JSONObject jsonobject = jsonarray.getJSONObject(i);
//                String id = jsonobject.getString("id");
//                String name = jsonobject.getString("name");
//                String text = jsonobject.getString("text");
//                String url = jsonobject.getString("url");
//                db.insert(id,name,text,url);
//            }
//            db.close();
//
//        } catch (Exception e) {
//            Log.d("error",e.getMessage());
//        }
//    }
    public String loadJSONFromAssetAadab() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("adab.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void insertadab(){
        try {
            DatabaseOperationsAdab db = new DatabaseOperationsAdab(getApplicationContext());
            JSONArray jsonarray = new JSONArray(loadJSONFromAssetAadab());
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String id = jsonobject.getString("id");
                String name = jsonobject.getString("name");
                String text = jsonobject.getString("text");
                db.insert(id,name,text);
            }
            db.close();

        } catch (Exception e) {
            Log.d("error",e.getMessage());
        }
    }
}
