package com.example.jonathan.arbaeen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jonathan.arbaeen.adapter.CityModel;
import com.example.jonathan.arbaeen.adapter.ViewPagerAdapter;
import com.example.jonathan.arbaeen.classes.Mysingleton;
import com.marcohc.toasteroid.Toasteroid;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    String where ="nowhere";
    ViewPagerAdapter viewPagerAdapter;
    Bundle bundle;
    SharedPreferences sp;
    String deviceid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        sp= getApplicationContext().getSharedPreferences("logininfo",0);
        bundle = getIntent().getExtras();
        where = bundle.getString("where");
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addfragment(new TodayFragment(),"امروز");
        viewPagerAdapter.addfragment(new CompassFragment(),"قبله یاب");
        viewPager.setAdapter(viewPagerAdapter);
        if(where.equals("quibla")){
            viewPager.setCurrentItem(1);
        }else
        {
            viewPager.setCurrentItem(0);
        }

        tabLayout.setupWithViewPager(viewPager);
//        Boolean stat = sp.getBoolean("installed",false);
//        if(!stat){
//            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//            deviceid = telephonyManager.getDeviceId();
//            if(deviceid==null){
//                deviceid= Build.SERIAL;
//            }
//            installed(deviceid);
//        }




    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this,IntroActivity.class));
        MainActivity.this.finish();
    }

//    public void installed(final String imei){
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        final String date = df.format(c.getTime());
//        SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
//        final String time = tf.format(c.getTime());
//
//
//        final String url=getResources().getString(R.string.url)+"installed.php";
//        StringRequest install = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                 if(response.equals("notok")||response.equals("exist")){
//                     Toast.makeText(MainActivity.this,"notOK",Toast.LENGTH_SHORT).show();
//                }else{
//                     Toast.makeText(MainActivity.this,"OK",Toast.LENGTH_SHORT).show();
//                     sp.edit().putBoolean("installed",true).apply();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this,"notOK",Toast.LENGTH_SHORT).show();
//            }
//        }){
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("imei",imei);
//                params.put("date",date);
//                params.put("time",time);
//                return params;
//            }
//        };
//        Mysingleton.getmInstance(MainActivity.this).addToRequestque(install);
//
//    }
}
