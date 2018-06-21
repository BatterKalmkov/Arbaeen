package com.example.jonathan.arbaeen.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonathan.arbaeen.NoheActivity;
import com.example.jonathan.arbaeen.R;
import com.example.jonathan.arbaeen.ViewZekrActivity;


import java.util.ArrayList;

import nl.changer.audiowife.AudioWife;

/**
 * Created by Jonathan on 9/9/2017.
 */

public class NoheplayAdapter extends RecyclerView.Adapter<NoheplayAdapter.RecyclerViewHolder> {
    private Context ctx;
    private ArrayList<NoheModel> arrayList,filterlist;


    public NoheplayAdapter(Context ctx, ArrayList<NoheModel> arrayList){
        this.arrayList=arrayList;
        this.ctx=ctx;
        this.filterlist=new ArrayList<NoheModel>();
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
        NoheModel noheModel = filterlist.get(position);
        holder.name.setText(noheModel.get_name());
        holder.id.setText(noheModel.get_parent());
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
            for(NoheModel item: arrayList){
                if(item.get_name().contains(title)){
                    filterlist.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name,id;
        Context ctx;
        SeekBar mMediaSeekBar;
        ImageView mPlayMedia,mPauseMedia;
        TextView mRunTime,mTotalTime;
        public RecyclerViewHolder(View view, Context context){
            super(view);
            this.ctx=context;
            name=(TextView)view.findViewById(R.id.azkar_item_name);
            id=(TextView)view.findViewById(R.id.azkar_item_id);
            mPlayMedia = (ImageView)view.findViewById(R.id.nohe_player_play);
            mPauseMedia =(ImageView) view.findViewById(R.id.nohe_player_pause);
            mMediaSeekBar = (SeekBar) view.findViewById(R.id.nohe_player_seekbar);
            mRunTime = (TextView) view.findViewById(R.id.nohe_player_runtime);
            mTotalTime = (TextView) view.findViewById(R.id.nohe_player_totaltime);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Uri mUri= Uri.parse(ctx.getResources().getString(R.string.nohe_url)+id.getText().toString()+"/"+name.getText().toString().replace(" ","_")+".mp3");
            NoheActivity.play(ctx,mUri);
//            Intent intent = new Intent(ctx, NoheActivity.class);
//            intent.putExtra("parent",id.getText().toString());
//            intent.putExtra("name",name.getText().toString());
//            this.ctx.startActivity(intent);

//            AudioWife audioWife = new AudioWife();
//            audioWife.init(ctx.getApplicationContext(), mUri);
//            audioWife.setPlayView(mPlayMedia);
//            audioWife.setPauseView(mPauseMedia);
//            audioWife.setSeekBar(mMediaSeekBar);
//            audioWife.setRuntimeView(mRunTime);
//            audioWife.setTotalTimeView(mTotalTime);
//            AudioWife.getInstance()
//                    .init(ctx.getApplicationContext(), mUri)
//                    .setPlayView(mPlayMedia)
//                    .setPauseView(mPauseMedia)
//                    .setSeekBar(mMediaSeekBar)
//                    .setRuntimeView(mRunTime)
//                    .setTotalTimeView(mTotalTime);

        }


    }


}