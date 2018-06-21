package com.example.jonathan.arbaeen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonathan.arbaeen.adapter.MessagesAdapter;
import com.example.jonathan.arbaeen.database.DatabaseOperations;
import com.example.jonathan.arbaeen.database.FBDatabaseOperations;

public class MessagesActivity extends AppCompatActivity {
    ImageView back;
    TextView toolbar_text;
    String title,message;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_text = (TextView)findViewById(R.id.toolbar_text);
        toolbar_text.setText("پیام ها");
        loadData(MessagesActivity.this);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MessagesActivity.this,IntroActivity.class));
        this.finish();
    }

    public static void loadData(Activity activity){
        FBDatabaseOperations db = new FBDatabaseOperations(activity);
        RecyclerView recyclerView = (RecyclerView)activity.findViewById(R.id.messages_recylcer);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        MessagesAdapter adapter = new MessagesAdapter(activity,activity.getApplicationContext(),db.getData());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        db.close();
    }
}
