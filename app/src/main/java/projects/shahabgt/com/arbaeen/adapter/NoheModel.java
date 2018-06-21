package com.example.jonathan.arbaeen.adapter;

/**
 * Created by Jonathan on 10/14/2017.
 */

public class NoheModel {
    private String name;
    private String object;
    private String parent;


    public String get_name(){
        return name.replace("_"," ");
    }
    public String get_object(){
        return object;
    }
    public String get_parent(){
        return parent;
    }

    public void set_name(String name){
        this.name = name;
    }
    public void set_object(String object){
        this.object = object;
    }
    public void set_parent(String parent){
        this.parent = parent;
    }
}
