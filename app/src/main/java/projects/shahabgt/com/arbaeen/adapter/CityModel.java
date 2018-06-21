package com.example.jonathan.arbaeen.adapter;

/**
 * Created by Jonathan on 9/2/2017.
 */

public class CityModel {
    private String city,lat,lon;

    public String get_city(){
        return city;
    }
    public String get_lat(){
        return lat;
    }
    public String get_lon(){
        return lon;
    }


    public void set_city(String city){
        this.city = city;
    }
    public void set_lat(String lat){
        this.lat = lat;
    }
    public void set_lon(String lon){
        this.lon = lon;
    }
}
