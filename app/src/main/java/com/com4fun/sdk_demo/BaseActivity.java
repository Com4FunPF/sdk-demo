package com.com4fun.sdk_demo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by airgoo on 2/28/18.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog pgd;
    void showProgress(String msg){
        if(pgd != null && pgd.isShowing()){
            pgd.dismiss();
        }

        pgd = ProgressDialog.show(this,"",msg,false,false);
    }

    void dismissProgress(){

        if(pgd != null && pgd.isShowing()){
            pgd.dismiss();
        }
    }

     void toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
