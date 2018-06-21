package com.example.jonathan.arbaeen;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jonathan.arbaeen.classes.BackgroundTask;
import com.example.jonathan.arbaeen.classes.EmailValidation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class SignupActivity extends AppCompatActivity {
    static final int gallery =20;
    static final int camera =50;
    int num;
    ImageView image;
    Uri uri;
    Uri fileUri;
    Bitmap bitmap;
    LinearLayout signup;
    EditText name,email,pass,repass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = (EditText)findViewById(R.id.signup_name);
        email = (EditText)findViewById(R.id.signup_email);
        pass = (EditText)findViewById(R.id.signup_pass);
        repass = (EditText)findViewById(R.id.signup_repass);

        image = (ImageView) findViewById(R.id.signup_avatar);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=1;
                new MaterialDialog.Builder(SignupActivity.this)
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
        signup = (LinearLayout)findViewById(R.id.signup_send);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (Exception e){}
                if(name.getText().toString().isEmpty()||email.getText().toString().isEmpty()||pass.getText().toString().isEmpty()){
                    Toast.makeText(SignupActivity.this,"لطفا موارد خواسته شده را پر کنید!",Toast.LENGTH_LONG).show();
                }else if(!(pass.getText().toString().equals(repass.getText().toString()))){
                    Toast.makeText(SignupActivity.this,"رمز و تکرار آن یکسان نیست!",Toast.LENGTH_LONG).show();
                }else if(!EmailValidation.isValid(email.getText().toString())){
                    Toast.makeText(SignupActivity.this,"ایمیل وارد شده معتبر نیست!",Toast.LENGTH_LONG).show();
                }else if(pass.length()<6){
                    Toast.makeText(SignupActivity.this,"رمز انتخابی باید بیشتر از 6 حرف باشد!",Toast.LENGTH_LONG).show();
                }else {
                    String pic = "";
                    if (bitmap != null) {
                        pic = imgToString(bitmap);
                    }
                    if (checknet()) {
                        BackgroundTask backgroundTask = new BackgroundTask(SignupActivity.this, 1);
                        backgroundTask.execute("signup",name.getText().toString(), email.getText().toString(), pass.getText().toString(),pic);
                    } else {
                        Toast.makeText(SignupActivity.this, "لطفا اتصال به اینترنت را بررسی کنید!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
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
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .setRequestedSize(1000,1000)
                            .start(this);
                    break;
                case camera:
                    CropImage.activity(fileUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .setActivityTitle("ادیت")
                            .setCropShape(CropImageView.CropShape.OVAL)
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
}
