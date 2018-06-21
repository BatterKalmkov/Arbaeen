package com.example.jonathan.arbaeen.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jonathan.arbaeen.R;
import com.example.jonathan.arbaeen.classes.DateParser;
import com.example.jonathan.arbaeen.database.FBDatabaseOperations;

import java.util.ArrayList;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private Uri uri;
    private Context ctx;
    private Activity activity;
    private ArrayList<MessagesModel> arrayList;


    public MessagesAdapter(Activity activity, Context ctx, ArrayList<MessagesModel> arrayList){
        this.arrayList=arrayList;
        this.ctx=ctx;
        this.activity =activity;
    }


    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_items,parent,false);
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(view,ctx,activity);
        return messagesViewHolder;


    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        MessagesModel messagesModelModel = arrayList.get(position);
        holder.id.setText(messagesModelModel.get_id());
        holder.title.setText(messagesModelModel.get_title());
        holder.message.setText(messagesModelModel.get_message());
        holder.sender.setText("فرستنده: "+messagesModelModel.get_sender());
        DateParser dp = new DateParser(messagesModelModel.get_date());
        DateModel dm = dp.dateAndTimeParser();
        String temp = dm.toString();
        holder.date.setText(temp);

    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class MessagesViewHolder extends RecyclerView.ViewHolder
    {

        TextView id,title,message,date,sender;
        ImageView delete;

        Context ctx;
        Activity act;

        public MessagesViewHolder(final View view, Context context,Activity activity){
            super(view);
            this.ctx=context;
            this.act=activity;
            id=(TextView)view.findViewById(R.id.messages_items_id);
            title=(TextView)view.findViewById(R.id.messages_items_title);
            message=(TextView)view.findViewById(R.id.messages_items_message);
            sender=(TextView)view.findViewById(R.id.messages_items_sender);
            date=(TextView)view.findViewById(R.id.messages_items_date);

            delete = (ImageView)view.findViewById(R.id.messages_items_delete);



            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(act)
                            .title("اطلاع")
                            .content("پیام حذف شود؟")
                            .positiveText("حدف")
                            .negativeText("بستن")
                            .positiveColor(Color.parseColor("#4caf50"))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    FBDatabaseOperations db = new FBDatabaseOperations(ctx);
                                    db.delete(act,id.getText().toString());
                                    db.close();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });

        }
    }
}