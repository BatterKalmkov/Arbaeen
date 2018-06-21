package com.example.jonathan.arbaeen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DatabaseOperationsNohe extends SQLiteOpenHelper {
    private static final int Database_Version = 1;
    private static final String Database_Name = "NoheDatabase";
    private static final String Table_Name = "nohetable";
    private static final String Create_query = "CREATE TABLE nohetable (object varchar(50), name varchar(100),parent varchar(100) );";


    public DatabaseOperationsNohe(Context context) {
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
    public void delete(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE "+Table_Name);
        db.execSQL(Create_query);
        }
    public boolean insert(String object,String name,String parent){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("object" , object);
        values.put("name" , name);
        values.put("parent" ,parent);
        if(db.insert(Table_Name, null ,values) > 0){
            return  true;
        }
        return false;
    }
    public String getfolder(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT name FROM nohetable WHERE object='Folder'";
        Cursor c =  db.rawQuery(query ,null);
        c.moveToFirst();
        JSONArray jsonArray = new JSONArray();
        if  (c.moveToFirst()) {
            do{
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", c.getString(c.getColumnIndex("name")));
                } catch (JSONException e) {}
                jsonArray.put(jsonObject);
            }while (c.moveToNext());
        }
        c.close();
        return jsonArray.toString();
    }

    public String getfiles(String folder){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT name,parent FROM nohetable WHERE object='File' AND parent='"+folder+"'";
        Cursor c =  db.rawQuery(query ,null);
        c.moveToFirst();
        JSONArray jsonArray = new JSONArray();
        if  (c.moveToFirst()) {
            do{
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", c.getString(c.getColumnIndex("name")).toLowerCase().replace(".mp3",""));
                    jsonObject.put("parent", c.getString(c.getColumnIndex("parent")));
                } catch (JSONException e) {}
                jsonArray.put(jsonObject);
            }while (c.moveToNext());
        }
        c.close();
        return jsonArray.toString();
    }
}
