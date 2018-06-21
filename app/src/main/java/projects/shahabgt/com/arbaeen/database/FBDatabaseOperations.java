package com.example.jonathan.arbaeen.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jonathan.arbaeen.MessagesActivity;
import com.example.jonathan.arbaeen.adapter.MessagesModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class FBDatabaseOperations extends SQLiteOpenHelper {
    private static final int Database_Version = 1;
    private static final String Database_Name = "MessagesDatabase";
    private static final String Table_Name = "messages";
    private static final String Create_query = "CREATE TABLE messages ( id varchar(25), title varchar(100),message varchar(500), sender varchar(100),date varchar(20));";


    public FBDatabaseOperations(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void close(){
        SQLiteDatabase db = getWritableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }}
    public boolean insert(Context context,String title,String message,String sender,String date){
        SharedPreferences sp = context.getSharedPreferences("logininfo", 0);
        String person = sp.getString("id", "");
        Calendar c = Calendar.getInstance();

        SimpleDateFormat ndf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        String ndate = ndf.format(c.getTime());
        SimpleDateFormat ntf = new SimpleDateFormat("HHmmss", Locale.ENGLISH);
        String ntime = ntf.format(c.getTime());
        String id = person + ndate + ntime;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id" , id);
        values.put("title" , title);
        values.put("message" ,message);
        values.put("sender" ,sender);
        values.put("date" , date);
        if(db.insert(Table_Name , null ,values) > 0){
            return  true;
        }
        return false;

    }


    public boolean delete(Activity activity,String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Table_Name,"id='"+id+"'",null);
        MessagesActivity.loadData(activity);
        return true;
    }

    public ArrayList<MessagesModel> getData(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * from messages ORDER BY date DESC;";
        Cursor cursor =  db.rawQuery(query ,null);
        ArrayList<MessagesModel> messagesModels = new ArrayList<MessagesModel>();
        if (cursor.moveToFirst()) {
            do {
                MessagesModel messagesModel = new MessagesModel();
                messagesModel.set_id(cursor.getString(cursor.getColumnIndex("id")));
                messagesModel.set_title(cursor.getString(cursor.getColumnIndex("title")));
                messagesModel.set_message(cursor.getString(cursor.getColumnIndex("message")));
                messagesModel.set_sender(cursor.getString(cursor.getColumnIndex("sender")));
                messagesModel.set_date(cursor.getString(cursor.getColumnIndex("date")));

                messagesModels.add(messagesModel);
            } while (cursor.moveToNext());
        }
        return messagesModels;
    }
}