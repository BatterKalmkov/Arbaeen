package com.example.jonathan.arbaeen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jonathan.arbaeen.classes.EmailValidation;
import com.example.jonathan.arbaeen.classes.Mysingleton;
import com.marcohc.toasteroid.Toasteroid;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ResetpassActivity extends AppCompatActivity {
    LinearLayout reset;
    EditText email;
    String pass;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass);
        builder = new AlertDialog.Builder(ResetpassActivity.this);
        reset=(LinearLayout)findViewById(R.id.reset_send);
        email=(EditText)findViewById(R.id.reset_email);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty()){
                    Toast.makeText(ResetpassActivity.this,"لطفا ایمیل خود را وارد کنید!",Toast.LENGTH_LONG).show();
                }else if(!EmailValidation.isValid(email.getText().toString())){
                    Toast.makeText(ResetpassActivity.this,"ایمیل وارد شده معتبر نیست!",Toast.LENGTH_LONG).show();
                }else {
                    pass=RandomPass();
                    Reset(email.getText().toString(),pass);

                }
            }
        });
    }
    protected String RandomPass() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 11) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

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

    public void DisplayMassage(String title, String message,final int stat){
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(stat==1){
                    ResetpassActivity.this.finish();
                }else {
                    email.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void Reset(final String email,final String pass){
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (Exception e){}
        if(checknet()){
            progressDialog= new ProgressDialog(ResetpassActivity.this);
            progressDialog.setMessage("لطفا منتظر بمانید...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String url=getResources().getString(R.string.url)+"resetpass.php";
            StringRequest loginrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("notexist")){
                        progressDialog.dismiss();
                        DisplayMassage("خطا!","فردی با مشخصات وارد شده موجود نمی باشد!",0);
                    }else if(response.equals("notok")){
                        progressDialog.dismiss();
                        DisplayMassage("خطا!","لطفا دوباره امتحان کنید!",0);
                    }else{
                        progressDialog.dismiss();
                        DisplayMassage("اطلاع!","رمز به ایمیل شما فرستاده شد!",1);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toasteroid.show(ResetpassActivity.this,"لطفا دوباره امتحان کنید.", Toasteroid.STYLES.ERROR);
                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("email",email);
                    return params;
                }
            };
            Mysingleton.getmInstance(ResetpassActivity.this).addToRequestque(loginrequest);

        }else{
            Toasteroid.show(ResetpassActivity.this,"اتصال به اینترنت را بررسی کنید!", Toasteroid.STYLES.ERROR);
        }
    }
}
