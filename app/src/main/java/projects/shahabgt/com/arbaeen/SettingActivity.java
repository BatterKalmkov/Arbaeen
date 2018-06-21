package com.example.jonathan.arbaeen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class SettingActivity extends AppCompatActivity {
    String version="";
    SharedPreferences sp;
    Element logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp= getApplicationContext().getSharedPreferences("logininfo",0);
        Element websiteElement = new Element();
        websiteElement.setTitle("وب سایت");
        websiteElement.setIconDrawable(R.drawable.ic_website);
//        websiteElement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://kanifanavar.ir/"));
//                startActivity(browserIntent);
//            }
//        });

        Element emailElement = new Element();
        emailElement.setTitle("ایمیل");
        emailElement.setIconDrawable(R.drawable.ic_mail_outline);
        emailElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"SDC_ICT@gmx.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "نرم افزار اجر");
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "خطا! دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Element telegramElement =  new Element();
        telegramElement.setTitle("کانال تلگرام");
        telegramElement.setIconDrawable(R.drawable.ic_teleg);
//        telegramElement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/kaniapp"));
//                startActivity(telegram);
//            }
//        });
        if(sp.getInt("stat",22)==1) {
            logout = new Element();
            logout.setTitle("خروج از حساب کاربری");
            logout.setIconDrawable(R.drawable.ic_setting_account);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.edit().putInt("stat", 0).apply();
                    new MaterialDialog.Builder(SettingActivity.this)
                            .content("با موفقیت از حساب کاربری خارج شدید")
                            .neutralText("بستن")
                            .cancelable(false)
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .show();
                }
            });
        }else {
            logout = new Element();
            logout.setTitle("ورود به حساب کاربری");
            logout.setIconDrawable(R.drawable.ic_setting_account);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    SettingActivity.this.finish();
                }
            });
        }


        Element mosq =  new Element();
        mosq.setTitle("اذان");
        mosq.setIconDrawable(R.drawable.ic_setting_azan);
        mosq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(SettingActivity.this)
                        .content("آیا مایل به پخش اذان هستید؟")
                        .positiveText("فعال")
                        .negativeText("غیر فعال")
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                sp.edit().putBoolean("azan",true).apply();
                                new MaterialDialog.Builder(SettingActivity.this)
                                        .content("لطفا برای اعمال تغییرات یکبار برنامه را ببندید و دوباره اجرا کنید")
                                        .neutralText("بستن")
                                        .show();
                                dialog.dismiss();

                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                sp.edit().putBoolean("azan",false).apply();
                                new MaterialDialog.Builder(SettingActivity.this)
                                        .content("لطفا برای اعمال تغییرات یکبار برنامه را ببندید و دوباره اجرا کنید")
                                        .neutralText("بستن")
                                        .show();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        Element abt =  new Element();
        abt.setTitle("درباره");
        abt.setIconDrawable(R.drawable.ic_setting_about);
        abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogClass2 cdd = new CustomDialogClass2(SettingActivity.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });

        Element osl =  new Element();
        osl.setTitle("OpenSource libraries");
        osl.setIconDrawable(R.drawable.ic_setting_opensource);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        }catch (Exception e){}

        Element ver =  new Element();
        ver.setTitle("ورژن"+version);
        ver.setIconDrawable(R.drawable.ic_setting_version);


        View aboutPage = new AboutPage(this)
                .isRTL(true)
                .setImage(R.drawable.logo)
                .setDescription("در این قسمت تنظیمات نرم افزار اجر را می توانید مشاهده کنید")
                .addGroup("تنظیمات")
                .addItem(mosq)
                .addItem(logout)
                .addGroup("ارتباط با ما")
               // .addItem(websiteElement)
              //  .addItem(telegramElement)
                .addItem(emailElement)
                .addGroup("درباره ما")
                .addItem(abt)
               // .addItem(osl)
                .addItem(ver)
                .create();
        setContentView(aboutPage);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingActivity.this,IntroActivity.class));
        SettingActivity.this.finish();
    }
}
