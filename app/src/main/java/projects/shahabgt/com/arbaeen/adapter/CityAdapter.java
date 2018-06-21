package com.example.jonathan.arbaeen.adapter;

/**
 * Created by Jonathan on 9/2/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jonathan.arbaeen.NewnazriActivity;
import com.example.jonathan.arbaeen.R;
import com.example.jonathan.arbaeen.ViewnazriActivity;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.RecyclerViewHolder> {
    private Context ctx;
    private ArrayList<CityModel> arrayList,filterlist;


    public CityAdapter(Context ctx, ArrayList<CityModel> arrayList){
        this.arrayList=arrayList;
        this.ctx=ctx;
        this.filterlist=new ArrayList<CityModel>();
        this.filterlist.addAll(this.arrayList);

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_city_items,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,ctx);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        CityModel cityModel = filterlist.get(position);
        holder.city.setText(cityModel.get_city());
        holder.lat.setText(cityModel.get_lat());
        holder.lon.setText(cityModel.get_lon());

    }

    @Override
    public int getItemCount() {

        return filterlist.size();
    }

    public void search (final String title){
        filterlist.clear();
        if(TextUtils.isEmpty(title))
        {
            filterlist.addAll(arrayList);

        }else{
            for(CityModel item: arrayList){
                if(item.get_city().contains(title)){
                    filterlist.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView city,lat,lon;
        Context ctx;
        SharedPreferences sp;
        public RecyclerViewHolder(View view, Context context){
            super(view);
            this.ctx=context;
            sp= ctx.getSharedPreferences("location",0);
            view.setOnClickListener(this);
            city=(TextView)view.findViewById(R.id.select_city_item_city);
            lat=(TextView)view.findViewById(R.id.select_city_item_lat);
            lon=(TextView)view.findViewById(R.id.select_city_item_lon);

        }

        @Override
        public void onClick(View v) {
            String[] data = city.getText().toString().split("-");
            if(sp.getInt("nazr",99)==3) {
                SharedPreferences.Editor e = sp.edit();
                e.putInt("stat", 1);
                e.putString("lat", lat.getText().toString());
                e.putString("lon", lon.getText().toString());
                e.putString("loc", data[1].trim());
                e.putInt("nazr",0);
                e.apply();
                ((Activity) ctx).finish();
            }else if(sp.getInt("nazr",99)==1) {
                SharedPreferences.Editor e = sp.edit();
                NewnazriActivity.selectlin1.setVisibility(View.VISIBLE);
                e.putString("city", data[1].trim());
                e.putInt("nazr",0);
                e.apply();
                ((Activity) ctx).finish();
            }else if(sp.getInt("nazr",99)==2){
                Intent intent = new Intent(ctx, ViewnazriActivity.class);
                intent.putExtra("location",data[1].trim());
                this.ctx.startActivity(intent);
                ((Activity) ctx).finish();
            }
            else if(sp.getInt("nazr",99)==4){
                ViewnazriActivity.adapter.searchcity(data[1].trim());
                ((Activity) ctx).finish();
            }


        }


    }


}
