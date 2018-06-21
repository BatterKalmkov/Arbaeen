package com.example.jonathan.arbaeen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jonathan.arbaeen.classes.BackgroundTask;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.marcohc.toasteroid.Toasteroid;

public class Viewnazridetails extends AppCompatActivity {
    Bundle bundle;
    String postid,personpost,person;
    TextView toolbar_text,city,title;
    TextView likec,dislikec,commentsc;
    LinearLayout linearLayout,errorlayout,loadinglayout,report,delete;
    SimpleDraweeView loadingimg,nointernet;
    LikeButton like,dislike;
    SharedPreferences sp;
    ImageView comments;
    String d,dn,report_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_viewnazridetails);
        sp= getApplicationContext().getSharedPreferences("logininfo",0);
        bundle = getIntent().getExtras();
        postid= bundle.getString("postid");
        person= sp.getString("person","");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        city = (TextView)findViewById(R.id.postdetails_state);
        title = (TextView)findViewById(R.id.postdetails_subject);
        city.setText(bundle.getString("city"));
        title.setText(bundle.getString("title"));
        setSupportActionBar(toolbar);
        toolbar_text = (TextView)findViewById(R.id.toolbar_text);
        toolbar_text.setText("اطلاعات موکب");
        linearLayout = (LinearLayout)findViewById(R.id.postdetails_layout);
        errorlayout = (LinearLayout)findViewById(R.id.postdetails_error);
        report = (LinearLayout)findViewById(R.id.postdetails_report);
        delete = (LinearLayout)findViewById(R.id.postdetails_delete);
        personpost=bundle.getString("person");
        if(personpost.equals(person)){
            report.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
        }else{
            report.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);}

        loadinglayout = (LinearLayout)findViewById(R.id.postdetails_loadinglayout);
        loadingimg = (SimpleDraweeView)findViewById(R.id.postdetails_loadingimg);
        Uri loadinguri= Uri.parse("asset:///loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(loadinguri)
                .setAutoPlayAnimations(true)
                .build();
        loadingimg.setController(controller);
        loadinglayout.setVisibility(View.VISIBLE);

        nointernet = (SimpleDraweeView)findViewById(R.id.postdetails_nointernet);
        Uri noinnetneturi= Uri.parse("asset:///nointernet.gif");
        DraweeController controller2 = Fresco.newDraweeControllerBuilder()
                .setUri(noinnetneturi)
                .setAutoPlayAnimations(true)
                .build();
        nointernet.setController(controller2);
        if(checknet()) {
            BackgroundTask backgroundTask = new BackgroundTask(Viewnazridetails.this, 0);
            backgroundTask.execute("getcount", postid,person);
        }else
        {
            loadinglayout.setVisibility(View.GONE);
            snackError();
        }
        likec = (TextView)findViewById(R.id.postdetails_like_c);
        dislikec = (TextView)findViewById(R.id.postdetails_dislike_c);
        commentsc = (TextView)findViewById(R.id.postdetails_comments_c);


        like = (LikeButton) findViewById(R.id.postdetails_like);
        dislike = (LikeButton) findViewById(R.id.postdetails_dislike);
        comments = (ImageView)findViewById(R.id.postdetails_comments);
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Viewnazridetails.this,ViewCommentsActivity.class);
                sp.edit().putString("postid",postid).apply();
                intent.putExtra("postid",bundle.getString("postid"));
                startActivity(intent);
            }
        });
        like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(checknet()) {
                    like.setLiked(true);
                    dislike.setLiked(false);
                    like.setEnabled(false);
                    dislike.setEnabled(false);
                    d = "1";
                    dn = "0";
                    BackgroundTask backgroundTask = new BackgroundTask(Viewnazridetails.this, 0);
                    backgroundTask.execute("dolike", postid, person, d, dn);
                }else {
                    Toasteroid.show(Viewnazridetails.this,"اتصال به اینترنت را بررسی کنید!", Toasteroid.STYLES.ERROR);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
            }
        });

        dislike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(checknet()){
                    dislike.setLiked(true);
                    like.setLiked(false);
                    dislike.setEnabled(false);
                    like.setEnabled(false);
                    d="0";
                    dn="1";
                    BackgroundTask backgroundTask = new BackgroundTask(Viewnazridetails.this, 0);
                    backgroundTask.execute("dolike", postid, person, d, dn);
                }else {
                    Toasteroid.show(Viewnazridetails.this,"اتصال به اینترنت را بررسی کنید!", Toasteroid.STYLES.ERROR);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(Viewnazridetails.this)
                        .title("گزارش")
                        .inputRangeRes(5, 100, R.color.red)
                        .content("لطفا دلیل خود را برای اعلام گزارش بیان کنید")
                        .inputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                        .positiveText("ارسال")
                        .negativeText("بستن")
                        .positiveColor(Color.parseColor("#4caf50"))
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .input(null,null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                report_text=input.toString();
                                if(checknet()){
                                    BackgroundTask backgroundTask = new BackgroundTask(Viewnazridetails.this, 1);
                                    backgroundTask.execute("postreport", postid, person,report_text);
                                }else {
                                    dialog.dismiss();
                                    Toasteroid.show(Viewnazridetails.this,"اتصال به اینترنت را بررسی کنید!", Toasteroid.STYLES.ERROR);

                                }

                            }
                        }).show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(Viewnazridetails.this)
                        .title("حذف")
                        .inputRangeRes(5, 100, R.color.red)
                        .content("لطفا دلیل خود را برای حذف مطلب بیان کنید")
                        .inputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                        .positiveText("ارسال")
                        .negativeText("بستن")
                        .positiveColor(Color.parseColor("#4caf50"))
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .input(null,null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                if(checknet()){
                                    BackgroundTask backgroundTask = new BackgroundTask(Viewnazridetails.this, 1);
                                    backgroundTask.execute("postdelete", postid,input.toString());
                                }else {
                                    dialog.dismiss();
                                    Toasteroid.show(Viewnazridetails.this,"اتصال به اینترنت را بررسی کنید!", Toasteroid.STYLES.ERROR);
                                }

                            }
                        }).show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        Viewnazridetails.this.finish();
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
    private void snackError(){
        loadinglayout.setVisibility(View.GONE);
        errorlayout.setVisibility(View.VISIBLE);
        final Snackbar snackbar = Snackbar
                .make(linearLayout, "اتصال به اینترنت را چک کنید", Snackbar.LENGTH_INDEFINITE)
                .setAction("تلاش مجدد", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // progressDialog.show();
                        if(checknet()){
                            errorlayout.setVisibility(View.GONE);
                            loadinglayout.setVisibility(View.VISIBLE);
                            BackgroundTask backgroundTask = new BackgroundTask(Viewnazridetails.this, 0);
                            backgroundTask.execute("postdetails", postid);}
                        else {
                            snackError();
                        }
                    }
                });

        snackbar.show();
    }
}
