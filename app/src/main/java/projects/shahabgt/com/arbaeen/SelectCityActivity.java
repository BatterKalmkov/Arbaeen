package com.example.jonathan.arbaeen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jonathan.arbaeen.adapter.CityAdapter;
import com.example.jonathan.arbaeen.adapter.CityModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SelectCityActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<CityModel> cityModels;
    RecyclerView.LayoutManager layoutManager;
    EditText search;
    Toolbar toolbar;
    TextView toolbar_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        city();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_text = (TextView)findViewById(R.id.toolbar_text);
        toolbar_text.setText("لیست شهر ها");
        layoutManager = new LinearLayoutManager(this);
        final CityAdapter adapter = new CityAdapter(this,cityModels );
        recyclerView = (RecyclerView) findViewById(R.id.select_city_recycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        search = (EditText)findViewById(R.id.select_city_search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.search(charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.search(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("city.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void city(){
        try {
            cityModels = new ArrayList<CityModel>();
            cityModels.clear();
            JSONArray jsonarray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < jsonarray.length(); i++) {

                CityModel cityModel = new CityModel();
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                cityModel.set_city(jsonobject.getString("city").trim().toLowerCase()+" - "+jsonobject.getString("cityF").trim());
                cityModel.set_lat(jsonobject.getString("lat")+"");
                cityModel.set_lon(jsonobject.getString("lon")+"");
                cityModels.add(cityModel);
            }
        } catch (Exception e) {
            Log.d("error",e.getMessage());
        }
    }
}
