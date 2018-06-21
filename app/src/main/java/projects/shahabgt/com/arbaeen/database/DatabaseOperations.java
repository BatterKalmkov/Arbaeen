package com.example.jonathan.arbaeen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DatabaseOperations extends SQLiteOpenHelper {
    private static final int Database_Version = 1;
    private static final String Database_Name = "AzkarDatabase";
    private static final String Table_Name = "azkartable";
    private static final String Create_query = "CREATE TABLE azkartable (name varchar(50), count varchar(100),text text);";


    public DatabaseOperations(Context context) {
        super(context, Database_Name, null, Database_Version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void close(){
        SQLiteDatabase db = getWritableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }}
    public boolean insert(String name,String count,String text){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name" , name);
        values.put("count" , count);
        values.put("text" ,text);
        if(db.insert(Table_Name, null ,values) > 0){
            return  true;
        }
        return false;
    }
    public String getAzkar(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT name,count,text FROM azkartable";
        Cursor c =  db.rawQuery(query ,null);
        c.moveToFirst();
        JSONArray jsonArray = new JSONArray();
        if  (c.moveToFirst()) {
            do{
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", c.getString(c.getColumnIndex("name")));
                    jsonObject.put("count", c.getString(c.getColumnIndex("count")));
                    jsonObject.put("text", c.getString(c.getColumnIndex("text")));
                } catch (JSONException e) {}
                jsonArray.put(jsonObject);
            }while (c.moveToNext());
        }
        c.close();
        return jsonArray.toString();
    }
    public String[] getContext(String name){
        String [] res=new String[2];
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT count,text FROM azkartable WHERE name='"+name+"';";
        Cursor c =  db.rawQuery(query ,null);
        c.moveToFirst();
        res[0] = c.getString(c.getColumnIndex("count"));
        res[1] = c.getString(c.getColumnIndex("text"));
        c.close();
        return res;
    }
}
