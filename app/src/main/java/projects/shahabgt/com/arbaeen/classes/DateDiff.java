package com.example.jonathan.arbaeen.classes;

import com.example.jonathan.arbaeen.adapter.DateModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jonathan on 9/16/2017.
 */

public class DateDiff {
    int nYear,nMonth,nDay;
    int oYear,oMonth,oDay;
    public DateDiff(String date){
        String[] data = date.split("-");
        this.oDay =Integer.parseInt(data[2]);
        this.oMonth=Integer.parseInt(data[1]);
        this.oYear = Integer.parseInt(data[0]);
    }

    public String diff(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String now = df.format(c.getTime());
        DateParser dp = new DateParser(now+"-11-11-11");
        DateModel dm = dp.dateAndTimeParser();
        String temp = dm.tonumberformat();
        String[] data = temp.split("-");
        nYear= Integer.parseInt(data[0]);
        nMonth= Integer.parseInt(data[1]);
        nDay= Integer.parseInt(data[2]);
        String res="";
        if(oYear>nYear || oYear==nYear && oMonth>nMonth ||oYear==nYear && oMonth==nMonth && oDay>nDay ){
            res= "توزیع: " +oYear+"/"+oMonth+"/"+oDay;
        }else if(nYear>oYear ||oYear==nYear && nMonth>oMonth || oYear==nYear && oMonth==nMonth && nDay>oDay)
        {
            res= "منقضی شده";
        }else if(oYear==nYear && oMonth==nMonth && oDay==nDay){
            res= "در حال توزیع";
        }
    return res;
    }
}
