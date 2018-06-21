package com.example.jonathan.arbaeen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baoyz.widget.PullRefreshLayout;
import com.example.jonathan.arbaeen.adapter.ViewnazriAdapter;
import com.example.jonathan.arbaeen.classes.BackgroundTask;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class ViewnazriActivity extends AppCompatActivity {
    Toolbar toolbar;
    FrameLayout toolbarcontainer;
    TextView toolbar_text;
    public static ViewnazriAdapter adapter;
    FloatingActionButton fab1,fab2;
    FloatingActionMenu fab;
    RecyclerView recyclerView;
    LinearLayout linearLayout,errorlayout,loadinglayout;
    PullRefreshLayout pullRefreshLayout;
    RecyclerView.LayoutManager layoutManager;
    SimpleDraweeView loadingimg,nointernet;
    MaterialSearchView searchView;
    MenuItem item;
    public static int mstate=0;
    SharedPreferences sp;
    Bundle bundle;
    public static String scity="all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(ViewnazriActivity.this);
        setContentView(R.layout.activity_viewnazri);
        sp = getApplicationContext().getSharedPreferences("location", 0);
        toolbarcontainer = (FrameLayout)findViewById(R.id.toolbar_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_text = (TextView)findViewById(R.id.toolbar_text);
        toolbar_text.setText("موکب های ثبت شده");
        fab = (FloatingActionMenu)findViewById(R.id.fab_menu);
        fab.setMenuButtonColorNormal(Color.parseColor("#1d4054"));
        fab.setMenuButtonColorPressed(Color.parseColor("#1d4054"));
        fab1 = (FloatingActionButton) findViewById(R.id.fab_item1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putInt("nazr",4).commit();
                fab.close(true);
                startActivity(new Intent(ViewnazriActivity.this,SelectCityActivity.class));
            }
        });
        fab2 = (FloatingActionButton) findViewById(R.id.fab_item2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addall();
                fab.close(true);
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.viewpost_recylcer);
        linearLayout = (LinearLayout)findViewById(R.id.viewpost_layout);
        errorlayout = (LinearLayout)findViewById(R.id.viewpost_error);
        pullRefreshLayout = (PullRefreshLayout)findViewById(R.id.viewpost_swipeRefreshLayout);


        layoutManager=new LinearLayoutManager(this);

        loadinglayout = (LinearLayout)findViewById(R.id.viewpost_loadinglayout);
        loadingimg = (SimpleDraweeView)findViewById(R.id.viewpost_loadingimg);
        Uri loadinguri= Uri.parse("asset:///loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(loadinguri)
                .setAutoPlayAnimations(true)
                .build();
        loadingimg.setController(controller);
        loadinglayout.setVisibility(View.VISIBLE);

        nointernet = (SimpleDraweeView)findViewById(R.id.viewpost_nointernet);
        Uri noinnetneturi= Uri.parse("asset:///nointernet.gif");
        DraweeController controller2 = Fresco.newDraweeControllerBuilder()
                .setUri(noinnetneturi)
                .setAutoPlayAnimations(true)
                .build();
        nointernet.setController(controller2);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                adapter.search(query);


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.search(newText);

                return true;
            }
        });
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checknet()) {
                    fab1.setLabelText("انتخاب شهر");
                    fab1.setEnabled(true);
                    BackgroundTask backgroundTask = new BackgroundTask(ViewnazriActivity.this, 0);
                    backgroundTask.execute("getposts");
                } else {
                    loadinglayout.setVisibility(View.VISIBLE);
                    snackError();
                    Toast.makeText(ViewnazriActivity.this,"اتصال به اینترنت را بررسی کنید!",Toast.LENGTH_LONG).show();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {

                    if (fab.isShown()) {
                        fab.hideMenuButton(true);
                    }
                }
                else if (dy <0) {

                    fab.showMenuButton(true);

                }
            }
        });
        if (checknet()) {
            BackgroundTask backgroundTask = new BackgroundTask(ViewnazriActivity.this, 0);
            backgroundTask.execute("getposts");

        } else {
            snackError();
            Toast.makeText(ViewnazriActivity.this,"اتصال به اینترنت را بررسی کنید!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(searchView.isSearchOpen()||fab.isOpened()) {
            searchView.closeSearch();
            fab.close(true);
        }else {
            startActivity(new Intent(ViewnazriActivity.this, IntroActivity.class));
            this.finish();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        if(mstate==0)
        {
            menu.getItem(0).setVisible(false);
        }
        else
        {   menu.getItem(0).setVisible(true);
        }


        return true;
    }
    @Override
    protected void onResume() {
        searchView.closeSearch();
        super.onResume();
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
        mstate=0;
        invalidateOptionsMenu();
        loadinglayout.setVisibility(View.GONE);
        errorlayout.setVisibility(View.VISIBLE);
        Snackbar snackbar = Snackbar
                .make(linearLayout, "متاسفانه خطایی اتفاق افتاده!!", Snackbar.LENGTH_INDEFINITE)
                .setAction("تلاش مجدد", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checknet()) {
                            loadinglayout.setVisibility(View.VISIBLE);
                            errorlayout.setVisibility(View.GONE);
                            BackgroundTask backgroundTask = new BackgroundTask(ViewnazriActivity.this, 0);
                            backgroundTask.execute("getposts");
                        }else {
                            snackError();
                        }
                    }
                });

        snackbar.show();
    }
}
