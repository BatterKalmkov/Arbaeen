package com.example.jonathan.arbaeen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DatabaseOperationsAdab extends SQLiteOpenHelper {
    private static final int Database_Version = 1;
    private static final String Database_Name = "AdabDatabase";
    private static final String Table_Name = "adabtable";
    private static final String Create_query = "CREATE TABLE adabtable (id varchar(50), name varchar(100),text text);";


    public DatabaseOperationsAdab(Context context) {
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
    public boolean insert(String id,String name,String text){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id" , id);
        values.put("name" , name);
        values.put("text" ,text);
        if(db.insert(Table_Name, null ,values) > 0){
            return  true;
        }
        return false;
    }
    public String getAdab(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT id,name,text FROM adabtable";
        Cursor c =  db.rawQuery(query ,null);
        c.moveToFirst();
        JSONArray jsonArray = new JSONArray();
        if  (c.moveToFirst()) {
            do{
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", c.getString(c.getColumnIndex("id")));
                    jsonObject.put("name", c.getString(c.getColumnIndex("name")));
                    jsonObject.put("text", c.getString(c.getColumnIndex("text")));
                } catch (JSONException e) {}
                jsonArray.put(jsonObject);
            }while (c.moveToNext());
        }
        c.close();
        return jsonArray.toString();
    }
    public String[] getContext(String id){
        String [] res=new String[2];
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT name,text FROM adabtable WHERE id='"+id+"';";
        Cursor c =  db.rawQuery(query ,null);
        c.moveToFirst();
        res[0] = c.getString(c.getColumnIndex("name"));
        res[1] = c.getString(c.getColumnIndex("text"));
        c.close();
        return res;
    }
}
