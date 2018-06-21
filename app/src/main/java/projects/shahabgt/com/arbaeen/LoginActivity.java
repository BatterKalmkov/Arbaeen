package com.example.jonathan.arbaeen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jonathan.arbaeen.classes.Mysingleton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.marcohc.toasteroid.Toasteroid;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button signin,signup,reset;
    EditText email,pass;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    SharedPreferences sp;
    String token,deviceid;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        builder = new AlertDialog.Builder(LoginActivity.this);
        sp= getApplicationContext().getSharedPreferences("logininfo",0);

        int stat = sp.getInt("stat",88);
        if(stat==1)
        {
            this.finish();
        }
        reset = (Button)findViewById(R.id.login_forogt);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResetpassActivity.class));
            }
        });
        email = (EditText)findViewById(R.id.login_email);
        pass=(EditText)findViewById(R.id.login_password);
        signin = (Button)findViewById(R.id.login_signin);
        signup = (Button)findViewById(R.id.login_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()||pass.getText().toString().isEmpty())
                {
                    Toasteroid.show(LoginActivity.this,"'ایمیل/رمز ورود' را وارد کنید", Toasteroid.STYLES.WARNING);
                }else{
                    token = sp.getString("token","gg");
                    if(token.length()<5){
                        token= FirebaseInstanceId.getInstance().getToken();
                    }
                    FirebaseMessaging.getInstance().subscribeToTopic("ajr_users");
                    TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    deviceid = telephonyManager.getDeviceId();
                    if(deviceid==null){
                        deviceid= Build.SERIAL;
                    }
                    logIn(email.getText().toString(), pass.getText().toString(),token,deviceid);
                }
            }
        });
    }
    private void logIn(final String email,final String passtext, final String token,final String deviceid){
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
        if(checknet()){
            progressDialog= new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("لطفا منتظر بمانید...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String url=getResources().getString(R.string.url)+"login.php";
            StringRequest loginrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("wrongpass")){
                        progressDialog.dismiss();
                        DisplayMassage("خطا!","رمز ورود وارد شده اشتباه می باشد!");

                    }else if(response.equals("notexist")){
                        progressDialog.dismiss();
                        DisplayMassage("خطا!","فردی با مشخصات وارد شده موجود نمی باشد!");
                    }else if(response.equals("notok")){
                        progressDialog.dismiss();
                        DisplayMassage("خطا!","لطفا دوباره امتحان کنید!");
                    }else{
                        progressDialog.dismiss();
                        SharedPreferences.Editor e=sp.edit();
                        e.putString("person",response);
                        e.putString("email",email);
                        e.putString("deviceid",deviceid);
                        e.putString("password",passtext);
                        e.putInt("stat",1);
                        e.apply();
                        Intent intent = new Intent(LoginActivity.this,IntroActivity.class);
                        intent.putExtra("where","nowhere");
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toasteroid.show(LoginActivity.this,"لطفا برنامه را ببندید و دوباره امتحان کنید.", Toasteroid.STYLES.ERROR);
                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("email",email);
                    params.put("password",passtext);
                    params.put("token",token);
                    params.put("deviceid",deviceid);
                    return params;
                }
            };
            Mysingleton.getmInstance(LoginActivity.this).addToRequestque(loginrequest);

        }else{
            Toasteroid.show(LoginActivity.this,"اتصال به اینترنت را بررسی کنید!", Toasteroid.STYLES.ERROR);
        }
    }

    private boolean checknet(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            return true;
        }else{
            return false;
        }
    }

    public void DisplayMassage(String title, String message){
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                email.setText("");
                pass.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this,IntroActivity.class);
        intent.putExtra("where","nowhere");
        startActivity(intent);
        LoginActivity.this.finish();
    }
}
