package com.example.jonathan.arbaeen.adapter;

/**
 * Created by Jonathan on 9/5/2017.
 */

public class NazriModel {
    private String postid;
    private String city;
    private String title;
    private String person;
    private String like;
    private String dislike;
    private String comment;
    private String nazrdate;


    public String get_postid(){
        return postid;
    }
    public String get_city(){
        return city;
    }
    public String get_title(){
        return title;
    }
    public String get_person(){
        return person;
    }
    public String get_like(){
        return like;
    }
    public String get_dislike(){
        return dislike;
    }
    public String get_comment(){
        return comment;
    }
    public String get_nazrdate(){
        return nazrdate;
    }

    public void set_postid(String postid){
        this.postid = postid;
    }
    public void set_city(String city){
        this.city = city;
    }
    public void set_title(String title){
        this.title = title;
    }
    public void set_person(String person){
        this.person = person;
    }
    public void set_like(String like){
        this.like = like;
    }
    public void set_dislike(String dislike){
        this.dislike = dislike;
    }
    public void set_comment(String comment){
        this.comment = comment;
    }
    public void set_nazrdate(String nazrdate){
        this.nazrdate = nazrdate;
    }


}
