package com.example.jonathan.arbaeen;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jonathan.arbaeen.adapter.NoheAdapter;
import com.example.jonathan.arbaeen.adapter.NoheModel;
import com.example.jonathan.arbaeen.adapter.NoheplayAdapter;
import com.example.jonathan.arbaeen.database.DatabaseOperationsNohe;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.changer.audiowife.AudioWife;

public class NoheActivity extends AppCompatActivity {
    DatabaseOperationsNohe dbnohe;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    TextView toolbar_text;
    Toolbar toolbar;
    FrameLayout toolbarcontainer;
    MaterialSearchView searchView;
    Bundle bundle;
    NoheplayAdapter adapternohe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nohe);
        bundle = getIntent().getExtras();
        toolbarcontainer = (FrameLayout)findViewById(R.id.toolbar_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_text = (TextView)findViewById(R.id.toolbar_text);
        toolbar_text.setText(bundle.getString("name").replace("_"," "));

        AudioWife audioWife = new AudioWife();
        searchView = (MaterialSearchView) findViewById(R.id.nohe_search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapternohe.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapternohe.search(newText);
                return true;
            }
        });
        dbnohe= new DatabaseOperationsNohe(getApplicationContext());
        layoutManager = new LinearLayoutManager(this);
        ArrayList<NoheModel> noheModels = new ArrayList<NoheModel>();
        noheModels.clear();
        try {
            JSONArray jsonarray = new JSONArray(dbnohe.getfiles(bundle.getString("name")));
            for (int i = 0; i < jsonarray.length(); i++) {

                NoheModel noheModel = new NoheModel();
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                noheModel.set_name(jsonobject.getString("name"));
                noheModel.set_parent(jsonobject.getString("parent"));
                noheModels.add(noheModel);
            }
            adapternohe = new NoheplayAdapter(this, noheModels);
            recyclerView = (RecyclerView) findViewById(R.id.nohe_recycler);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapternohe);
            dbnohe.close();
        } catch (Exception e) {
        }


    }

    @Override
    public void onBackPressed() {
        AudioWife.getInstance().pause();
        AudioWife.getInstance().release();
        super.onBackPressed();
    }

    public static void play(Context context, Uri uri){
        AudioWife.getInstance().pause();
        AudioWife.getInstance().release();
        Activity activity = (Activity)context;
        SeekBar mMediaSeekBar;
        ImageView mPlayMedia,mPauseMedia;
        TextView mRunTime,mTotalTime;
        mPlayMedia = (ImageView)activity.findViewById(R.id.nohe_player_play);
        mPauseMedia =(ImageView) activity.findViewById(R.id.nohe_player_pause);
        mMediaSeekBar = (SeekBar) activity.findViewById(R.id.nohe_player_seekbar);
        mRunTime = (TextView) activity.findViewById(R.id.nohe_player_runtime);
        mTotalTime = (TextView) activity.findViewById(R.id.nohe_player_totaltime);
        AudioWife.getInstance()
                .init(activity.getApplicationContext(), uri)
                    .setPlayView(mPlayMedia)
                    .setPauseView(mPauseMedia)
                    .setSeekBar(mMediaSeekBar)
                    .setRuntimeView(mRunTime)
                    .setTotalTimeView(mTotalTime)
                    .play();
    }
}
