package com.example.jonathan.arbaeen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.jonathan.arbaeen.adapter.AdabAdapter;
import com.example.jonathan.arbaeen.adapter.AdabModel;
import com.example.jonathan.arbaeen.adapter.AzkarAdapter;
import com.example.jonathan.arbaeen.adapter.AzkarModel;
import com.example.jonathan.arbaeen.adapter.CityAdapter;
import com.example.jonathan.arbaeen.adapter.DoaAdapter;
import com.example.jonathan.arbaeen.adapter.DoaModel;
import com.example.jonathan.arbaeen.adapter.NoheAdapter;
import com.example.jonathan.arbaeen.adapter.NoheModel;
import com.example.jonathan.arbaeen.database.DatabaseOperations;
import com.example.jonathan.arbaeen.database.DatabaseOperationsAdab;
import com.example.jonathan.arbaeen.database.DatabaseOperationsDoa;
import com.example.jonathan.arbaeen.database.DatabaseOperationsNohe;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AzkarActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    DatabaseOperations db;
    DatabaseOperationsDoa dbdoa;
    DatabaseOperationsNohe dbnohe;
    DatabaseOperationsAdab dbadab;
    MaterialSearchView searchView;
    AzkarAdapter adapter;
    DoaAdapter adapterdoa;
    NoheAdapter adapternohe;
    AdabAdapter adapteradab;
    String where;
    TextView toolbar_text;
    Toolbar toolbar;
    FrameLayout toolbarcontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_azkar);
        toolbarcontainer = (FrameLayout)findViewById(R.id.toolbar_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_text = (TextView)findViewById(R.id.toolbar_text);
        searchView = (MaterialSearchView) findViewById(R.id.azkar_search_view);
        where = getIntent().getExtras().getString("where");
        if(where.equals("azkar")){
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
            toolbar_text.setText("اذکار");
            db= new DatabaseOperations(getApplicationContext());
            layoutManager = new LinearLayoutManager(this);
            ArrayList<AzkarModel> azkarModels = new ArrayList<AzkarModel>();
            azkarModels.clear();
            try {
                JSONArray jsonarray = new JSONArray(db.getAzkar());
                for (int i = 0; i < jsonarray.length(); i++) {

                    AzkarModel azkarModel = new AzkarModel();
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    azkarModel.set_name(jsonobject.getString("name"));
                    azkarModels.add(azkarModel);
                }
                adapter = new AzkarAdapter(this, azkarModels);
                recyclerView = (RecyclerView) findViewById(R.id.azkar_recycler);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                db.close();
            } catch (Exception e) {
            }
        }else if(where.equals("doa")){
            searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapterdoa.search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapterdoa.search(newText);
                    return true;
                }
            });
            toolbar_text.setText("ادعیه");
            dbdoa= new DatabaseOperationsDoa(getApplicationContext());
            layoutManager = new LinearLayoutManager(this);
            ArrayList<DoaModel> doaModels = new ArrayList<DoaModel>();
            doaModels.clear();
            try {
                JSONArray jsonarray = new JSONArray(dbdoa.getDoa());
                for (int i = 0; i < jsonarray.length(); i++) {

                    DoaModel doaModel = new DoaModel();
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    doaModel.set_name(jsonobject.getString("name"));
                    doaModel.set_id(jsonobject.getString("id"));
                    doaModels.add(doaModel);
                }
                adapterdoa = new DoaAdapter(this, doaModels);
                recyclerView = (RecyclerView) findViewById(R.id.azkar_recycler);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapterdoa);
                dbdoa.close();
            } catch (Exception e) {
            }
        }else if(where.equals("nohe")){
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
            toolbar_text.setText("نوحه");
            dbnohe= new DatabaseOperationsNohe(getApplicationContext());
            layoutManager = new LinearLayoutManager(this);
            ArrayList<NoheModel> noheModels = new ArrayList<NoheModel>();
            noheModels.clear();
            try {
                JSONArray jsonarray = new JSONArray(dbnohe.getfolder());
                for (int i = 0; i < jsonarray.length(); i++) {

                    NoheModel noheModel = new NoheModel();
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    noheModel.set_name(jsonobject.getString("name"));
                    noheModels.add(noheModel);
                }
                adapternohe = new NoheAdapter(this, noheModels);
                recyclerView = (RecyclerView) findViewById(R.id.azkar_recycler);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapternohe);
                dbnohe.close();
            } catch (Exception e) {
            }
        }else if(where.equals("adab")){
            searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapteradab.search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapteradab.search(newText);
                    return true;
                }
            });
            toolbar_text.setText("آداب اربعین");
            dbadab= new DatabaseOperationsAdab(getApplicationContext());
            layoutManager = new LinearLayoutManager(this);
            ArrayList<AdabModel> adabModels = new ArrayList<AdabModel>();
            adabModels.clear();
            try {
                JSONArray jsonarray = new JSONArray(dbadab.getAdab());
                for (int i = 0; i < jsonarray.length(); i++) {

                    AdabModel adabModel = new AdabModel();
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    adabModel.set_name(jsonobject.getString("name"));
                    adabModel.set_id(jsonobject.getString("id"));
                    adabModels.add(adabModel);
                }
                adapteradab = new AdabAdapter(this, adabModels);
                recyclerView = (RecyclerView) findViewById(R.id.azkar_recycler);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapteradab);
                dbadab.close();
            } catch (Exception e) {
            }
        }



    }

    @Override
    public void onBackPressed() {
        if(searchView.isSearchOpen()){
            searchView.closeSearch();
        }else {
            startActivity(new Intent(AzkarActivity.this,IntroActivity.class));
            this.finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }
}
