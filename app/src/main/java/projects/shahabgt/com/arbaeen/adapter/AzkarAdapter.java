package com.example.jonathan.arbaeen.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jonathan.arbaeen.R;
import com.example.jonathan.arbaeen.ViewZekrActivity;


import java.util.ArrayList;

/**
 * Created by Jonathan on 9/9/2017.
 */

public class AzkarAdapter extends RecyclerView.Adapter<AzkarAdapter.RecyclerViewHolder> {
    private Context ctx;
    private ArrayList<AzkarModel> arrayList,filterlist;


    public AzkarAdapter(Context ctx, ArrayList<AzkarModel> arrayList){
        this.arrayList=arrayList;
        this.ctx=ctx;
        this.filterlist=new ArrayList<AzkarModel>();
        this.filterlist.addAll(this.arrayList);

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.azkar_items,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,ctx);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        AzkarModel azkarModel = filterlist.get(position);
        holder.name.setText(azkarModel.get_name());
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
            for(AzkarModel item: arrayList){
                if(item.get_name().contains(title)){
                    filterlist.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        Context ctx;
        public RecyclerViewHolder(View view, Context context){
            super(view);
            this.ctx=context;
            view.setOnClickListener(this);
            name=(TextView)view.findViewById(R.id.azkar_item_name);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ctx, ViewZekrActivity.class);
            intent.putExtra("name",name.getText().toString());
            intent.putExtra("audio","no");
            intent.putExtra("where","zekr");
            this.ctx.startActivity(intent);
        }


    }


}