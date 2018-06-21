package com.example.jonathan.arbaeen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jonathan.arbaeen.R;
import com.example.jonathan.arbaeen.Viewnazridetails;
import com.example.jonathan.arbaeen.classes.DateDiff;

import java.util.ArrayList;

/**
 * Created by Jonathan on 9/5/2017.
 */

public class ViewnazriAdapter extends RecyclerView.Adapter<ViewnazriAdapter.RecyclerViewHolder> {
    private Context ctx;
    private ArrayList<NazriModel> arrayList,filterlist;


    public ViewnazriAdapter(Context ctx, ArrayList<NazriModel> arrayList){
        this.arrayList=arrayList;
        this.ctx=ctx;
        this.filterlist=new ArrayList<NazriModel>();
        this.filterlist.addAll(this.arrayList);

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,ctx);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        NazriModel nazriModel = filterlist.get(position);
        holder.title.setText(nazriModel.get_title());
        DateDiff dateDiff = new DateDiff(nazriModel.get_nazrdate());
        holder.nazrdate.setText(dateDiff.diff());
        holder.city.setText(nazriModel.get_city());
        holder.postid.setText(nazriModel.get_postid());
        holder.person.setText(nazriModel.get_person());
        holder.like.setText(nazriModel.get_like());
        holder.dislike.setText(nazriModel.get_dislike());
        holder.comment.setText(nazriModel.get_comment());

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
            for(NazriModel item: arrayList){
                if(item.get_title().contains(title)){
                    filterlist.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void searchcity (final String title){
        filterlist.clear();
        if(TextUtils.isEmpty(title))
        {
            filterlist.addAll(arrayList);

        }else{
            for(NazriModel item: arrayList){
                if(item.get_city().contains(title)){
                    filterlist.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void addall (){
        filterlist.addAll(arrayList);
        notifyDataSetChanged();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title,city,nazrdate,postid,like,dislike,comment,person;
        Context ctx;
        public RecyclerViewHolder(View view, Context context){
            super(view);
            this.ctx=context;
            view.setOnClickListener(this);
            title=(TextView)view.findViewById(R.id.recycler_item_title);
            postid=(TextView)view.findViewById(R.id.recycler_item_postid);
            nazrdate=(TextView)view.findViewById(R.id.recycler_item_nazrdate);
            city=(TextView)view.findViewById(R.id.recycler_item_city);
            person=(TextView)view.findViewById(R.id.recycler_item_person);
            like=(TextView)view.findViewById(R.id.recycler_item_likes);
            dislike=(TextView)view.findViewById(R.id.recycler_item_dislikes);
            comment=(TextView)view.findViewById(R.id.recycler_item_comments);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ctx,Viewnazridetails.class);
            intent.putExtra("postid",postid.getText());
            intent.putExtra("title",title.getText());
            //intent.putExtra("nazrdate",nazrdate.getText());
            intent.putExtra("city",city.getText());
            intent.putExtra("person",person.getText());
            this.ctx.startActivity(intent);

        }


    }


}

