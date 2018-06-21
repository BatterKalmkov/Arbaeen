package com.example.jonathan.arbaeen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonathan.arbaeen.database.DatabaseOperations;
import com.example.jonathan.arbaeen.database.DatabaseOperationsAdab;
import com.example.jonathan.arbaeen.database.DatabaseOperationsDoa;
import com.example.jonathan.arbaeen.database.DatabaseOperationsNohe;

import java.io.File;

import nl.changer.audiowife.AudioWife;

public class ViewZekrActivity extends AppCompatActivity {
    Bundle bundle;
    TextView zekr,context;
    SeekBar mMediaSeekBar;
    ImageView mPlayMedia,mPauseMedia;
    TextView mRunTime,mTotalTime;
    private static final int INTENT_PICK_AUDIO = 1;
    private Context mContext;
    Uri mUri;
    String where;
    LinearLayout player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_zekr);
        zekr = (TextView)findViewById(R.id.viewzekr_zekr);
        context = (TextView)findViewById(R.id.viewzekr_context);
        player=(LinearLayout)findViewById(R.id.viewzekr_player);
        mPlayMedia = (ImageView)findViewById(R.id.player_play);
        mPauseMedia =(ImageView) findViewById(R.id.player_pause);
        mMediaSeekBar = (SeekBar) findViewById(R.id.player_seekbar);
        mRunTime = (TextView) findViewById(R.id.player_runtime);
        mTotalTime = (TextView) findViewById(R.id.player_totaltime);
        bundle = getIntent().getExtras();
        if(bundle.getString("audio").equals("yes")){
            player.setVisibility(View.VISIBLE);
        }

        where=bundle.getString("where");
        mContext=this;
        if(where.equals("zekr")){
           DatabaseOperations db = new DatabaseOperations(getApplicationContext());
            String[] extra = db.getContext(bundle.getString("name"));
            zekr.setText(bundle.getString("name")+" ("+ extra[0]+" بار)");
            context.setText(extra[1]);
        }else if(where.equals("doa")){
            player.setVisibility(View.VISIBLE);
            DatabaseOperationsDoa db = new DatabaseOperationsDoa(getApplicationContext());
            String id =bundle.getString("id");
            String[] extra = db.getContext(id);
            zekr.setText(extra[0]);
            context.setText(extra[1]);
            Uri mUri= Uri.parse(getResources().getString(R.string.doa_url)+id+".mp3");
            AudioWife.getInstance()
                    .init(getApplicationContext(), mUri)
                    .setPlayView(mPlayMedia)
                    .setPauseView(mPauseMedia)
                    .setSeekBar(mMediaSeekBar)
                    .setRuntimeView(mRunTime)
                    .setTotalTimeView(mTotalTime);
        }else if(where.equals("nohe")){
//            DatabaseOperationsNohe db = new DatabaseOperationsNohe(getApplicationContext());
//            String[] extra = db.getContext(bundle.getString("id"));
//            zekr.setText(extra[0]);
//            context.setText(extra[1]);
//            mUri= Uri.parse(extra[2]);
//            AudioWife.getInstance()
//                    .init(getApplicationContext(), mUri)
//                    .setPlayView(mPlayMedia)
//                    .setPauseView(mPauseMedia)
//                    .setSeekBar(mMediaSeekBar)
//                    .setRuntimeView(mRunTime)
//                    .setTotalTimeView(mTotalTime);
        }else if(where.equals("adab")){
            DatabaseOperationsAdab db = new DatabaseOperationsAdab(getApplicationContext());
            String[] extra = db.getContext(bundle.getString("id"));
            zekr.setText(extra[0]);
            context.setText(extra[1]);
        }


    }

    @Override
    public void onBackPressed() {
        AudioWife.getInstance().pause();
        AudioWife.getInstance().release();
        player.setVisibility(View.GONE);
        super.onBackPressed();
    }
}
