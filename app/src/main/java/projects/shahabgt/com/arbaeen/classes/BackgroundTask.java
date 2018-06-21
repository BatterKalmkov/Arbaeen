package com.example.jonathan.arbaeen.classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.jonathan.arbaeen.AzkarActivity;
import com.example.jonathan.arbaeen.IntroActivity;
import com.example.jonathan.arbaeen.R;
import com.example.jonathan.arbaeen.ViewnazriActivity;
import com.example.jonathan.arbaeen.adapter.CommentsAdapter;
import com.example.jonathan.arbaeen.adapter.CommentsModel;
import com.example.jonathan.arbaeen.adapter.DateModel;
import com.example.jonathan.arbaeen.adapter.NazriModel;
import com.example.jonathan.arbaeen.adapter.ViewnazriAdapter;
import com.example.jonathan.arbaeen.database.DatabaseOperationsAdab;
import com.example.jonathan.arbaeen.database.DatabaseOperationsNohe;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.like.LikeButton;
import com.marcohc.toasteroid.Toasteroid;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jonathan on 9/4/2017.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {
    Context ctx;
    Activity activity;
    int stat;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder,builder2;
    String method;
    LinearLayout errorlayout,linearLayout,loadinglayout;
    LinearLayout errorlayout1,linearLayout1,loadinglayout1,errorlayout2,linearLayout2,loadinglayout2;
    PullRefreshLayout pullRefreshLayout;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ViewnazriAdapter adapter;
    CommentsAdapter adapter2;
    TextView date,time,address,likec,dislikec,commentsc,nazrdate;
    SimpleDraweeView img;
    Uri uri;
    String didlike,postidx,person,scroll;
    LikeButton like,dislike;
    RelativeLayout relativeLayout;
    EditText etext;

    public BackgroundTask(Context context, int stat){

        this.ctx = context;
        this.activity = (Activity)context;
        this.stat=stat;

    }

    @Override
    protected void onPreExecute() {
        builder = new AlertDialog.Builder(activity);
        builder2 = new AlertDialog.Builder(activity);
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("لطفا منتظر بمانید...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        if(stat==1) {
            progressDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        method = params[0];
        if(method.equals("insert")) {
                SharedPreferences sp = activity.getSharedPreferences("logininfo",0);
                final String urlLink = ctx.getResources().getString(R.string.url)+"insert.php";
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String date = df.format(c.getTime());
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                String time = tf.format(c.getTime());

                SimpleDateFormat ndf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
                String ndate = ndf.format(c.getTime());
                SimpleDateFormat ntf = new SimpleDateFormat("HHmmss", Locale.ENGLISH);
                String ntime = ntf.format(c.getTime());
                String person = sp.getString("person","");
                String postid = person + ndate + ntime;
                
                try {
                    URL url = new URL(urlLink);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String data =
                                    URLEncoder.encode("postid", "UTF-8") + "=" + URLEncoder.encode(postid, "UTF-8") + "&" +
                                    URLEncoder.encode("person", "UTF-8") + "=" + URLEncoder.encode(person, "UTF-8") + "&" +
                                    URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                                    URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&" +
                                    URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8") + "&" +
                                    URLEncoder.encode("pic", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                    URLEncoder.encode("picurl", "UTF-8") + "=" + URLEncoder.encode(postid, "UTF-8") + "&" +
                                    URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8") + "&" +
                                    URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&" +
                                    URLEncoder.encode("stat", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8")+ "&" +
                                    URLEncoder.encode("nazrdate", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();

                    } catch (Exception e) {
                        return "noway1";
                    }
        }else if (method.equals("getposts"))
        {
            final String urlLink = ctx.getResources().getString(R.string.url)+"ngetposts.php";

            try {
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                return "noway2";
            }

        }else if(method.equals("postdetails")){
            final String urlLink = ctx.getResources().getString(R.string.url)+"postdetails.php";
            postidx = params[1];
            person  = params[2];
            try {
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("postid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                return "noway3";
            }
        }else if(method.equals("signup")){
            final String urlLink = ctx.getResources().getString(R.string.url)+"signup.php";
            try {
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8")+ "&" +
                                URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8")+ "&" +
                                URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8")+ "&" +
                                URLEncoder.encode("pic", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                return "noway4";
            }
        }else if(method.equals("dolike")){
            final String urlLink = ctx.getResources().getString(R.string.url)+"dolike.php";
            didlike=params[3];
            postidx = params[1];
            person  = params[2];
            try {
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =URLEncoder.encode("postid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("person", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8")+ "&" +
                        URLEncoder.encode("like", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8")+ "&" +
                        URLEncoder.encode("dislike", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                return "noway5";
            }

        }else if(method.equals("getlikes")){
            final String urlLink =ctx.getResources().getString(R.string.url)+"getlikes.php";
            postidx=params[1];
            person=params[2];
            try {
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =URLEncoder.encode("postid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("person", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                return "noway6";
            }

        }else if (method.equals("postreport"))
        {
            final String urlLink = ctx.getResources().getString(R.string.url)+"postreport.php";
            try{
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String date = df.format(c.getTime());
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                String time = tf.format(c.getTime());

                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =URLEncoder.encode("postid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("person", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8")+ "&" +
                        URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8")+ "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")+ "&" +
                        URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e){
                return "noway7";
            }
        }else if (method.equals("postdelete"))
        {
            final String urlLink = ctx.getResources().getString(R.string.url)+"postdelete.php";
            try{
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String date = df.format(c.getTime());
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                String time = tf.format(c.getTime());

                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =URLEncoder.encode("postid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8")+ "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")+ "&" +
                        URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e){
                return "noway8";
            }
        }else if(method.equals("getcount")){
            final String urlLink = ctx.getResources().getString(R.string.url)+"getcommentscount.php";
            postidx = params[1];
            person  = params[2];
            try {
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("postid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                return "noway9";
            }

        }else if(method.equals("getcomments")) {
            scroll=params[3];
            final String urlLink = ctx.getResources().getString(R.string.url)+"getcomments.php";
            try {
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("postid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                URLEncoder.encode("person", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                return "noway10";
            }
        }else if(method.equals("insertcomment")) {
            scroll=params[5];
            final String urlLink = ctx.getResources().getString(R.string.url)+"insercomment.php";
            postidx = params[1];
            person = params[2];
            Calendar c = Calendar.getInstance();
            SimpleDateFormat ndf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
            String ndate = ndf.format(c.getTime());
            SimpleDateFormat ntf = new SimpleDateFormat("HHmmss", Locale.ENGLISH);
            String ntime = ntf.format(c.getTime());
            String cmid = "c"+person + ndate + ntime;

            try {
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("cmid", "UTF-8") + "=" + URLEncoder.encode(cmid, "UTF-8") + "&" +
                                URLEncoder.encode("postid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                URLEncoder.encode("cmperson", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                                URLEncoder.encode("cmcomment", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&" +
                                URLEncoder.encode("cmtime", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                return "noway11";
            }
        }else if (method.equals("doclike"))
        {   postidx=params[1];
            person=params[3];
            scroll=params[4];
            final String urlLink = ctx.getResources().getString(R.string.url)+"doclike.php";
            try{
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =URLEncoder.encode("cmid", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                        URLEncoder.encode("person", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e){
                return "noway12";
            }

        }else if (method.equals("commentdelete"))
        {scroll=params[4];
            final String urlLink = ctx.getResources().getString(R.string.url)+"commentdelete.php";
            try{
                postidx = params[2];
                person = params[3];
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =URLEncoder.encode("cmid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e){
                return "noway13";
            }
        }else if (method.equals("commentreport"))
        {   scroll=params[4];
            final String urlLink = ctx.getResources().getString(R.string.url)+"commentreport.php";
            try{
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String date = df.format(c.getTime());
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                String time = tf.format(c.getTime());

                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =URLEncoder.encode("cmid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("person", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8")+ "&" +
                        URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8")+ "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")+ "&" +
                        URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e){
                return "noway14";
            }
        }else if (method.equals("getnohe"))
        {
            final String urlLink = ctx.getResources().getString(R.string.url)+"getnohe.php";
            try{
                URL url = new URL(urlLink);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e){
                return "noway15";
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        like = (LikeButton) activity.findViewById(R.id.postdetails_like);
        dislike = (LikeButton) activity.findViewById(R.id.postdetails_dislike);
        loadinglayout = (LinearLayout) activity.findViewById(R.id.viewpost_loadinglayout);
        linearLayout = (LinearLayout) activity.findViewById(R.id.viewpost_layout);
        errorlayout = (LinearLayout) activity.findViewById(R.id.viewpost_error);
        loadinglayout1 = (LinearLayout) activity.findViewById(R.id.postdetails_loadinglayout);
        linearLayout1 = (LinearLayout) activity.findViewById(R.id.postdetails_layout);
        errorlayout1 = (LinearLayout) activity.findViewById(R.id.postdetails_error);
        if(method.equals("insert")){
            progressDialog.dismiss();
            if(response.equals("noway1"))
            {
                DisplayMassage("خطا!","لطفا دوباره امتحان کنید.",0);
            }
            else if(response.equals("ok"))
            {
                DisplayMassage("اطلاع!","با موفقیت ثبت شد!",1);
            }else if(response.equals("notok")) {
                DisplayMassage("خطا!","لطفا دوباره امتحان کنید.",0);
            }else{
                DisplayMassage("خطا!",response.toString(),0);
            }

        }else if(method.equals("getposts")){
            if (response.equals("noway2")) {
                errorlayout.setVisibility(View.VISIBLE);
                loadinglayout.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "متاسفانه خطایی اتفاق افتاده!!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("بازگشت", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.finish();
                            }
                        });

                snackbar.show();
            }else if (response.equals("nothing")){
                DisplayMassage("خطا","موکبی پیدا نشد",1);

            }else {
                pullRefreshLayout = (PullRefreshLayout) activity.findViewById(R.id.viewpost_swipeRefreshLayout);
                recyclerView = (RecyclerView) activity.findViewById(R.id.viewpost_recylcer);
                ArrayList<NazriModel> nazriModels = new ArrayList<NazriModel>();
                nazriModels.clear();
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {

                        NazriModel nazriModel = new NazriModel();
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        nazriModel.set_postid(jsonobject.getString("postid"));
                        nazriModel.set_title(jsonobject.getString("title"));
                        nazriModel.set_city(jsonobject.getString("city"));
                        nazriModel.set_nazrdate(jsonobject.getString("nazrdate"));
                        nazriModel.set_like(jsonobject.getString("likes"));
                        nazriModel.set_dislike(jsonobject.getString("dislikes"));
                        nazriModel.set_comment(jsonobject.getString("comments"));
                        nazriModel.set_person(jsonobject.getString("person"));
                        nazriModels.add(nazriModel);
                    }

                    layoutManager = new LinearLayoutManager(activity);
                    adapter = new ViewnazriAdapter(activity, nazriModels);
                    ViewnazriActivity.adapter = adapter;
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    loadinglayout.setVisibility(View.GONE);
                    errorlayout.setVisibility(View.GONE);
                    pullRefreshLayout.setRefreshing(false);
                    ViewnazriActivity.mstate = 1;
                    activity.invalidateOptionsMenu();
                } catch (Exception e) {
                    Toast.makeText(activity,"خطا لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();

                }
            }
        }else if(method.equals("postdetails")){
            if(response.equals("noway3"))
            {
                errorlayout1.setVisibility(View.VISIBLE);
                loadinglayout1.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "متاسفانه خطایی اتفاق افتاده!!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("بازگشت", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.finish();
                            }
                        });

                snackbar.show();
            } else if(!response.equals("nothing"))
            {
                time = (TextView)activity.findViewById(R.id.postdetails_time);
                date = (TextView)activity.findViewById(R.id.postdetails_date);
                img = (SimpleDraweeView)activity.findViewById(R.id.postdetails_img);
                address=(TextView)activity.findViewById(R.id.postdetails_address);
                nazrdate = (TextView)activity.findViewById(R.id.postdetails_nazrdate);
                String[] data = response.split("::");
                address.setText(data[0]);
                DateParser dp = new DateParser(data[1]+"-11-11-11");
                DateModel dm = dp.dateAndTimeParser();
                String temp = dm.toString();
                date.setText(temp);
                time.setText(data[2]);

                String[] nazrtime=data[5].split("-");
                String nazryear = nazrtime[0];
                String nazrmonth = nazrtime[1];
                switch (nazrmonth){
                    case "1":
                        nazrmonth = " فروردین ";
                        break;
                    case "2":
                        nazrmonth = " اردیبهشت ";
                        break;
                    case "3":
                        nazrmonth = " خرداد ";
                        break;
                    case "4":
                        nazrmonth = " تیر ";
                        break;
                    case "5":
                        nazrmonth = " مرداد ";
                        break;
                    case "6":
                        nazrmonth = " شهریور ";
                        break;
                    case "7":
                        nazrmonth = " مهر ";
                        break;
                    case "8":
                        nazrmonth = " آبان ";
                        break;
                    case "9":
                        nazrmonth = " آذر ";
                        break;
                    case "10":
                        nazrmonth = " دی ";
                        break;
                    case "11":
                        nazrmonth = " بهمن ";
                        break;
                    case "12":
                        nazrmonth = " اسفند ";
                        break;
                }
                String nazrday = nazrtime[2];
                String nazrhour = nazrtime[3];
                String nazrmin = nazrtime[4];
                nazrdate.setText(nazrday+nazrmonth+nazryear+" ساعت "+nazrhour+":"+nazrmin);


                if(data[3].length()>5) {
                    uri = Uri.parse(ctx.getResources().getString(R.string.img_url) + data[3] + ".jpg");
                }else {
                    uri = Uri.parse(ctx.getResources().getString(R.string.otherimg_url) + "noimage.png");
                }
                img.setImageURI(uri);
                final ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
                progressBarDrawable.setColor(ctx.getResources().getColor(R.color.colorPrimaryDark));
                progressBarDrawable.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
                img.getHierarchy().setProgressBarImage(progressBarDrawable);
                img.setController(
                        Fresco.newDraweeControllerBuilder()
                                .setTapToRetryEnabled(true)
                                .setUri(uri)
                                .build());
                BackgroundTask backgroundTask =new BackgroundTask(activity,0);
                backgroundTask.execute("getlikes",postidx,person);

            }else
            {
                DisplayMassage("خطا!","لطفا دوباره امتحان کنید.",1);
            }
        }else if(method.equals("signup")){
            progressDialog.dismiss();
            if(response.equals("noway4"))
            {
                DisplayMassage("خطا!","لطفا دوباره امتحان کنید.",0);
            }
            else if(response.equals("ok"))
            {
                DisplayMassage("اطلاع!","ثبت نام با موفقیت انجام شد!",2);
            }else if(response.equals("notok")) {
                DisplayMassage("خطا!","لطفا دوباره امتحان کنید.",0);
            }else if(response.equals("exist")) {
                DisplayMassage("خطا!","حساب کاربری با ایمیل وارد شده موجود است!",0);
            }

        }else if(method.equals("dolike")) {


            if (response.equals("noway5")) {

            } else {
                if (didlike.equals("1")) {
                    dislike.setEnabled(true);
                } else if (didlike.equals("0")) {
                    like.setEnabled(true);
                }
                BackgroundTask backgroundTask = new BackgroundTask(activity, 0);
                backgroundTask.execute("getlikes", postidx, person);
            }
        }else if(method.equals("getlikes")){
            if(response.equals("noway6")){
                errorlayout1.setVisibility(View.VISIBLE);
                loadinglayout1.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(linearLayout1, "متاسفانه خطایی اتفاق افتاده!!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("بازگشت", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.finish();
                            }
                        });

                snackbar.show();
            }else if(!response.isEmpty()) {
                likec = (TextView) activity.findViewById(R.id.postdetails_like_c);
                dislikec = (TextView) activity.findViewById(R.id.postdetails_dislike_c);
                like = (LikeButton) activity.findViewById(R.id.postdetails_like);
                dislike = (LikeButton) activity.findViewById(R.id.postdetails_dislike);
                String[] datax = response.split("::");
                likec.setText(datax[0]);
                dislikec.setText(datax[1]);
                String wyd=datax[2];
                if(wyd.equals("1"))
                {   like.setLiked(true);
                    like.setEnabled(false);
                    dislike.setEnabled(true);
                    dislike.setLiked(false);
                }else if(wyd.equals("0"))
                {   dislike.setLiked(true);
                    dislike.setEnabled(false);
                    like.setEnabled(true);
                    like.setLiked(false);
                }
                loadinglayout1.setVisibility(View.GONE);
                errorlayout1.setVisibility(View.GONE);

            }
        }else if(method.equals("postreport")) {
            progressDialog.dismiss();
            if(response.equals("noway6")||response.equals("notok")) {
                Toasteroid.show(activity,"خطا لطفا دوباره امتحان کنید!", Toasteroid.STYLES.ERROR);
            }else if(response.equals("did")){
                Toasteroid.show(activity,"قبلا این مطلب را گزارش داده اید!", Toasteroid.STYLES.INFO);
            }else if(response.equals("ok")){
                Toasteroid.show(activity,"با موفقیت گزارش شد!", Toasteroid.STYLES.SUCCESS);
            }
        }else if(method.equals("postdelete")) {
            progressDialog.dismiss();
            if(response.equals("noway7")||response.equals("notok")) {
                Toasteroid.show(activity,"خطا لطفا دوباره امتحان کنید!", Toasteroid.STYLES.ERROR);
            }else if(response.equals("did")){
                Toasteroid.show(activity,"قبلا درخواست حذف این مطلب داده اید!", Toasteroid.STYLES.INFO);
            }else if(response.equals("ok")){
                Toasteroid.show(activity,"در خواست حذف با موفقیت ارسال شد!", Toasteroid.STYLES.SUCCESS);
            }
        }else if(method.equals("getcount")){
            if(response.equals("noway9"))
            {
                errorlayout1.setVisibility(View.VISIBLE);
                loadinglayout1.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(linearLayout1, "متاسفانه خطایی اتفاق افتاده!!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("بازگشت", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.finish();
                            }
                        });

                snackbar.show();
            }else {
                commentsc = (TextView) activity.findViewById(R.id.postdetails_comments_c);
                commentsc.setText(response);
                BackgroundTask backgroundTask = new BackgroundTask(activity, 0);
                backgroundTask.execute("postdetails", postidx, person);
            }
        }else if(method.equals("getcomments")) {
            loadinglayout2 = (LinearLayout)activity.findViewById(R.id.viewcomments_loadinglayout);
            errorlayout2 = (LinearLayout)activity.findViewById(R.id.viewcomments_error);
            relativeLayout = (RelativeLayout) activity.findViewById(R.id.viewcomments_layout);
            recyclerView = (RecyclerView) activity.findViewById(R.id.viewcomments_recylcer);

            if (response.equals("noway10")) {
                errorlayout2.setVisibility(View.VISIBLE);
                loadinglayout2.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, "متاسفانه خطایی اتفاق افتاده!!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("بازگشت", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.finish();
                            }
                        });

                snackbar.show();
            } else if (!response.equals("nothing")) {

                ArrayList<CommentsModel> commentsModels = new ArrayList<CommentsModel>();
                commentsModels.clear();
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {

                        CommentsModel commentsModel = new CommentsModel();
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        commentsModel.set_cmid(jsonobject.getString("cmid"));
                        commentsModel.set_cmperson(jsonobject.getString("cmperson"));
                        commentsModel.set_comment(jsonobject.getString("comment"));
                        DateParser dp = new DateParser(jsonobject.getString("time"));
                        DateModel dm = dp.dateAndTimeParser();
                        String temp = dm.toString();
                        commentsModel.set_time(temp);
                        commentsModel.set_name(jsonobject.getString("name"));
                        commentsModel.set_avatar(jsonobject.getString("avatar"));
                        commentsModel.set_likes(jsonobject.getString("likes"));
                        commentsModel.set_didlike(jsonobject.getString("didlike"));
                        commentsModels.add(commentsModel);
                    }

                    layoutManager = new LinearLayoutManager(activity);
                    adapter2 = new CommentsAdapter(activity, commentsModels);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter2);
                    if(scroll.equals("do")) {
                        recyclerView.scrollToPosition(adapter2.getItemCount() - 1);
                    }
                    loadinglayout2.setVisibility(View.GONE);
                    errorlayout2.setVisibility(View.GONE);
                } catch (Exception e) {
                    Toasteroid.show(activity, "خطا لطفا دوباره امتحان کنید!", Toasteroid.STYLES.ERROR);
                }

            }else if (response.equals("nothing")) {
                recyclerView.setAdapter(null);
                loadinglayout2.setVisibility(View.GONE);
                errorlayout2.setVisibility(View.GONE);

            }
        }else if(method.equals("insertcomment")) {
            etext = (EditText)activity.findViewById(R.id.viewcomments_text);
            if(response.equals("noway11")||response.equals("notok")){
                progressDialog.dismiss();
                DisplayMassage("خطا!","لطفا دوباره امتحان کنید.",0);
            }else if(response.equals("ok")){
                etext.setText("");
                progressDialog.dismiss();
                BackgroundTask backgroundTask =new BackgroundTask(activity,0);
                backgroundTask.execute("getcomments",postidx,person,scroll);
            }
        }else if(method.equals("doclike")) {
            etext = (EditText)activity.findViewById(R.id.viewcomments_text);
            if(response.equals("noway12")||response.equals("notok")){
                DisplayMassage("خطا!","لطفا دوباره امتحان کنید.",0);
            }else if(response.equals("ok")){
                BackgroundTask backgroundTask =new BackgroundTask(activity,0);
                backgroundTask.execute("getcomments",postidx,person,scroll);
            }
        }else if(method.equals("commentdelete")) {
            if(response.equals("noway13")||response.equals("notok")){
                progressDialog.dismiss();
                DisplayMassage("خطا!","لطفا دوباره امتحان کنید.",0);
            }else if(response.equals("ok")){
                progressDialog.dismiss();
                BackgroundTask backgroundTask =new BackgroundTask(activity,0);
                backgroundTask.execute("getcomments",postidx,person,scroll);
            }
        }else if(method.equals("commentreport")) {
            progressDialog.dismiss();
            if(response.equals("noway14")||response.equals("notok")) {
                Toasteroid.show(activity,"خطا لطفا دوباره امتحان کنید!", Toasteroid.STYLES.ERROR);
            }else if(response.equals("did")){
                Toasteroid.show(activity,"قبلا این نظر را گزارش داده اید!", Toasteroid.STYLES.INFO);
            }else if(response.equals("ok")){
                Toasteroid.show(activity,"با موفقیت گزارش شد!", Toasteroid.STYLES.SUCCESS);
            }
        }else if(method.equals("getnohe")){
            if (response.equals("noway15")) {
                Toast.makeText(activity,"خطا لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }else {
                try{
                    DatabaseOperationsNohe db = new DatabaseOperationsNohe(ctx);
                    db.delete();
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String object = jsonobject.getString("Object");
                        String name = jsonobject.getString("Name");
                        String parent = jsonobject.getString("ParrentName");
                        db.insert(object,name,parent);
                    }
                    db.close();
                    progressDialog.dismiss();
                    Intent intent =new Intent(activity,AzkarActivity.class);
                    intent.putExtra("where","nohe");
                    activity.startActivity(intent);
                    activity.finish();
                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(activity,"خطا لطفا دوباره امتحان کنید!",Toast.LENGTH_LONG).show();

                }
            }
        }

    }

    private void DisplayMassage(String title, String message,final int stat){
        builder2.setTitle(title);
        builder2.setMessage(message);
        builder2.setPositiveButton("بستن", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (stat==1){
                    activity.startActivity(new Intent(activity, IntroActivity.class));
                    activity.finish();

                }else if(stat==2){
                    activity.finish();
                }

            }
        });
        AlertDialog alertDialog = builder2.create();
        alertDialog.show();
    }
}
