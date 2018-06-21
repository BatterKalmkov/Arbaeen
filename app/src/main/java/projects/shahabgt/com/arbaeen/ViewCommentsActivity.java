package com.example.jonathan.arbaeen;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jonathan.arbaeen.adapter.CommentsAdapter;
import com.example.jonathan.arbaeen.classes.BackgroundTask;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.like.LikeButton;
import com.marcohc.toasteroid.Toasteroid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ViewCommentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CommentsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageView back,send;
    EditText text;
    Bundle bundle;
    TextView toolbar_text,cmid;
    SharedPreferences sp;
    LinearLayout loadinglayout,errorlayout,commentlinear;
    RelativeLayout relativeLayout;
    SimpleDraweeView loadingimg,nointernet;
    LikeButton heart;
    String person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_text = (TextView)findViewById(R.id.toolbar_text);
        toolbar_text.setText("نظرات");

        recyclerView=(RecyclerView)findViewById(R.id.viewcomments_recylcer);
        layoutManager=new LinearLayoutManager(this);
        loadinglayout = (LinearLayout)findViewById(R.id.viewcomments_loadinglayout);
        errorlayout = (LinearLayout)findViewById(R.id.viewcomments_error);
        commentlinear = (LinearLayout)findViewById(R.id.viewcomments_linear);
        relativeLayout = (RelativeLayout)findViewById(R.id.viewcomments_layout);
        cmid = (TextView)findViewById(R.id.comments_items_cmid);
        heart = (LikeButton)findViewById(R.id.comments_items_heart);
        sp= getApplicationContext().getSharedPreferences("logininfo",0);
        person = sp.getString("person", "");
        bundle = getIntent().getExtras();
        final String postid = bundle.getString("postid");

        loadingimg = (SimpleDraweeView)findViewById(R.id.viewcomments_loadingimg);
        Uri loadinguri= Uri.parse("asset:///loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(loadinguri)
                .setAutoPlayAnimations(true)
                .build();
        loadingimg.setController(controller);
        loadinglayout.setVisibility(View.VISIBLE);
        commentlinear.setEnabled(false);

        nointernet = (SimpleDraweeView)findViewById(R.id.viewcomments_nointernet);
        Uri noinnetneturi= Uri.parse("asset:///nointernet.gif");
        DraweeController controller2 = Fresco.newDraweeControllerBuilder()
                .setUri(noinnetneturi)
                .setAutoPlayAnimations(true)
                .build();
        nointernet.setController(controller2);

        if(checknet()){
            BackgroundTask backgroundTask =new BackgroundTask(ViewCommentsActivity.this,0);
            backgroundTask.execute("getcomments",postid,person,"do");
        }else {
            snackError(postid);
        }


        text = (EditText)findViewById(R.id.viewcomments_text);

        send=(ImageView)findViewById(R.id.viewcomments_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (Exception e){}
                if(!text.getText().toString().isEmpty()){
                    if(checknet()){
                        sp= getApplicationContext().getSharedPreferences("logininfo",0);
                        String person= sp.getString("person","");
                        Calendar c = Calendar.getInstance();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String da = df.format(c.getTime());
                        SimpleDateFormat tf = new SimpleDateFormat("HH-mm-ss",Locale.ENGLISH);
                        String ti = tf.format(c.getTime());
                        String time = da+"-"+ti;
                        BackgroundTask backgroundTask =new BackgroundTask(ViewCommentsActivity.this,1);
                        backgroundTask.execute("insertcomment",postid,person,text.getText().toString(),time,"do");
                    }else {
                        Toasteroid.show(ViewCommentsActivity.this,"لطفا اتصال به اینترنت را بررسی کنید", Toasteroid.STYLES.ERROR);
                    }

                }else {
                    Toasteroid.show(ViewCommentsActivity.this,"لطفا پیام خود را وارد کنید", Toasteroid.STYLES.INFO);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        ViewCommentsActivity.this.finish();
    }
    private void snackError(final String postid){
        loadinglayout.setVisibility(View.GONE);
        commentlinear.setVisibility(View.GONE);
        errorlayout.setVisibility(View.VISIBLE);
        Snackbar snackbar = Snackbar
                .make(relativeLayout, "متاسفانه خطایی اتفاق افتاده!!", Snackbar.LENGTH_INDEFINITE)
                .setAction("تلاش مجدد", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checknet()) {
                            loadinglayout.setVisibility(View.VISIBLE);
                            errorlayout.setVisibility(View.GONE);
                            BackgroundTask backgroundTask = new BackgroundTask(ViewCommentsActivity.this,0);
                            backgroundTask.execute("getcomments",postid);
                        }else{
                            snackError(postid);
                        }

                    }
                });

        snackbar.show();
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
}
