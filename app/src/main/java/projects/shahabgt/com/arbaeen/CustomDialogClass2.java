package com.example.jonathan.arbaeen;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Jonathan on 10/17/2017.
 */

public class CustomDialogClass2 extends Dialog implements
        android.view.View.OnClickListener {
    public Activity c;

    public CustomDialogClass2(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog2);

    }


    @Override
    public void onClick(View v) {
       dismiss();
    }
}
