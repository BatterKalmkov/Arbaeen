package com.example.jonathan.arbaeen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jonathan.arbaeen.adapter.DateModel;
import com.example.jonathan.arbaeen.classes.BackgroundTask;
import com.example.jonathan.arbaeen.classes.DateParser;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class NewnazriActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    static final int gallery =20;
    static final int camera =50;
    ImageView image;
    AlertDialog.Builder builder;
    EditText title,address;
    Bitmap bitmap=null;
    Uri uri;
    Uri fileUri;
    int num;
    public static LinearLayout select1,selectlin1,select2,selectlin2,send;
    public static TextView selecttext,selecttext2;
    SharedPreferences sp;
    String nazritime,nazrdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_newnazri);
        builder = new AlertDialog.Builder(NewnazriActivity.this);
        sp= getApplication().getSharedPreferences("location",0);
        send=(LinearLayout)findViewById(R.id.newpost_send);
        image = (ImageView) findViewById(R.id.newpost_img);
        title=(EditText)findViewById(R.id.newpost_title);
        address=(EditText)findViewById(R.id.newpost_address);
        select1 = (LinearLayout)findViewById(R.id.newpost_select1);
        selectlin1= (LinearLayout)findViewById(R.id.newpost_selectLin1);
        selecttext = (TextView)findViewById(R.id.newpost_selectText1);
        select2 = (LinearLayout)findViewById(R.id.newpost_select2);
        selectlin2= (LinearLayout)findViewById(R.id.newpost_selectLin2);
        selecttext2 = (TextView)findViewById(R.id.newpost_selectText2);
        select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putInt("nazr",1).commit();
                startActivity(new Intent(NewnazriActivity.this,SelectCityActivity.class));
            }
        });
        select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersianCalendar persianCalendar = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        NewnazriActivity.this,
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay()
                );
                datePickerDialog.setThemeDark(true);
                datePickerDialog.show(getFragmentManager(), "انتخاب تاریخ");

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (Exception e){}
                if(title.getText().toString().isEmpty()||address.getText().toString().isEmpty()||selecttext.getText().toString().isEmpty()||selecttext2.getText().toString().isEmpty()){
                    Toast.makeText(NewnazriActivity.this,"لطفا موارد خواسته شده را پر کنید!",Toast.LENGTH_LONG).show();
                }else {
                    String pic = "";
                    if (bitmap != null) {
                        pic = imgToString(bitmap);
                    }
                    if (checknet()) {
                        BackgroundTask backgroundTask = new BackgroundTask(NewnazriActivity.this, 1);
                        backgroundTask.execute("insert", pic, selecttext.getText().toString(), title.getText().toString(), address.getText().toString(),nazritime);
                    } else {
                        Toast.makeText(NewnazriActivity.this, "لطفا اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
                    }
                }
            }

        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=1;
                new MaterialDialog.Builder(NewnazriActivity.this)
                        .title("انتخاب تصویر")
                        .items("از گالری","استفاده از دوربین")
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if(which==0)
                                {
                                    gallerySelect();
                                }else{
                                    captureImage();
                                }
                                return true;
                            }
                        })
                        .show();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        selecttext.setText(sp.getString("city",""));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NewnazriActivity.this,IntroActivity.class));
        this.finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            switch (requestCode){
                case gallery:
                    uri = data.getData();
                    CropImage.activity(uri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .setActivityTitle("ادیت")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setRequestedSize(1000,1000)
                            .start(this);
                    break;
                case camera:
                    CropImage.activity(fileUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .setActivityTitle("ادیت")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setRequestedSize(1000,1000)
                            .start(this);
                    break;
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try{
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                            image.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private boolean checknet(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            return true;
        }else{
            return false;
        }
    }
    private String imgToString (Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, camera);
    }
    private void gallerySelect(){
        Intent galleryPickerIntent = new Intent();
        galleryPickerIntent.setType("image/*");
        galleryPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryPickerIntent, "انتخاب تصویر"), gallery);
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type) {
        String IMAGE_DIRECTORY_NAME ="Ajr";

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String now = df.format(c.getTime());
        DateParser dp = new DateParser(now+"-11-11-11");
        DateModel dm = dp.dateAndTimeParser();
        String temp = dm.tonumberformat();
        String[] data = temp.split("-");
        int nYear= Integer.parseInt(data[0]);
        int nMonth= Integer.parseInt(data[1]);
        int nDay= Integer.parseInt(data[2]);
        if(nYear>year ||year==nYear && nMonth>(monthOfYear+1) || year==nYear && (monthOfYear+1)==nMonth && nDay>dayOfMonth)
        {
            Toast.makeText(NewnazriActivity.this,"در انتخاب تاریخ دقت کنید!",Toast.LENGTH_LONG).show();
        }else {
            nazritime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + "-";
            nazrdate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth + " ";
            PersianCalendar persianCalendar = new PersianCalendar();
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                    NewnazriActivity.this,
                    persianCalendar.get(PersianCalendar.HOUR_OF_DAY),
                    persianCalendar.get(PersianCalendar.MINUTE),
                    true
            );
            timePickerDialog.setThemeDark(true);
            timePickerDialog.show(getFragmentManager(), "انتخاب زمان");
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        nazritime = nazritime + hourOfDay+"-"+minute;
        nazrdate = nazrdate +"ساعت "+ hourOfDay+":"+minute;
        selectlin2.setVisibility(View.VISIBLE);
        selecttext2.setText(nazrdate);

    }
}
