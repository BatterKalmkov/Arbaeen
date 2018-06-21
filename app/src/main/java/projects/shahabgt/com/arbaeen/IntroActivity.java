package com.example.jonathan.arbaeen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jonathan.arbaeen.R;
import com.example.jonathan.arbaeen.adapter.AzkarModel;
import com.example.jonathan.arbaeen.classes.QuiblaCalculator;
import com.marcohc.toasteroid.Toasteroid;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;

public class IntroActivity extends AppCompatActivity {
    LinearLayout today,azkar,mosque,nazr,kabba,setting,rekat,messages,pedo,rosary;
    Intent intent;
    SharedPreferences sp,sp2;
    boolean doubleBackToExitPressedOnce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        doubleBackToExitPressedOnce = false;
        sp = getApplicationContext().getSharedPreferences("location", 0);
        sp2= getApplicationContext().getSharedPreferences("logininfo",0);
        final int stat = sp2.getInt("stat",88);
        today = (LinearLayout)findViewById(R.id.intro_today);
        azkar = (LinearLayout)findViewById(R.id.intro_azkar);
        mosque = (LinearLayout)findViewById(R.id.intro_mosque);
        nazr = (LinearLayout)findViewById(R.id.intro_nazr);
        kabba = (LinearLayout)findViewById(R.id.intro_kabba);
        setting = (LinearLayout)findViewById(R.id.intro_setting);
        rekat = (LinearLayout)findViewById(R.id.intro_rekat);
        rosary = (LinearLayout)findViewById(R.id.intro_rosary);
        messages = (LinearLayout)findViewById(R.id.intro_messages);
        pedo = (LinearLayout)findViewById(R.id.intro_pedo);

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(IntroActivity.this, nazr);
        droppyBuilder.addMenuItem(new DroppyMenuItem("ثبت موکب جدید"))
                .addSeparator()
                .addMenuItem(new DroppyMenuItem("مشاهده موکب های ثبت شده"))
                .addSeparator()
                .setPopupAnimation(new DroppyFadeInAnimation());

        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                switch (id){
                    case 0:
                            intent = new Intent(IntroActivity.this, NewnazriActivity.class);
                            startActivity(intent);
                            IntroActivity.this.finish();

                        break;
                    case 1:
                        intent = new Intent(IntroActivity.this, ViewnazriActivity.class);
                        startActivity(intent);
                        IntroActivity.this.finish();
                        break;
                }
            }
        });
        if(stat==1) {
            DroppyMenuPopup droppyMenu = droppyBuilder.build();
        }else {
            nazr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                    IntroActivity.this.finish();
                }
            });
        }

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(IntroActivity.this,MainActivity.class);
                intent.putExtra("where","nowhere");
                startActivity(intent);
                IntroActivity.this.finish();
            }
        });
        azkar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(IntroActivity.this,AzkarActivity.class));
              //  IntroActivity.this.finish();

                    CustomDialogClass cdd = new CustomDialogClass(IntroActivity.this);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();

            }
        });
        mosque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !checknet()) {
                    new MaterialDialog.Builder(IntroActivity.this)
                            .title("خطا!")
                            .content("برای استفاده از این سرویس اینترنت و GPS خود را فعال کنید")
                            .positiveText("ورود بدون فعال سازی")
                            .neutralText("بستن")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    startActivity(new Intent(IntroActivity.this,MosquesActivity.class));
                                    dialog.dismiss();
                                    IntroActivity.this.finish();
                                }
                            })
                            .show();
                }else {
                    startActivity(new Intent(IntroActivity.this,MosquesActivity.class));
                    IntroActivity.this.finish();
                }
            }
        });
        kabba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(IntroActivity.this,MainActivity.class);
                intent.putExtra("where","quibla");
                startActivity(intent);
                IntroActivity.this.finish();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this,SettingActivity.class));
                IntroActivity.this.finish();
            }
        });
        rekat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this,RekatActivity.class));
                IntroActivity.this.finish();
            }
        });
        rosary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this,RosaryActivity.class));
                IntroActivity.this.finish();
            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this,MessagesActivity.class));
                IntroActivity.this.finish();
            }
        });
        pedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this,PedoActivity.class));
                IntroActivity.this.finish();
            }
        });

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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            IntroActivity.this.finish();
            Toasteroid.dismiss();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toasteroid.show(IntroActivity.this,"برای خروج دوباره کلید بازگشت را فشار دهید!", Toasteroid.STYLES.INFO);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
