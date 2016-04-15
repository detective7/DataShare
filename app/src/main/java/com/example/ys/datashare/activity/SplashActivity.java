package com.example.ys.datashare.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.example.ys.datashare.R;

public class SplashActivity extends Activity {

    boolean isLogin = false;
    private static final int GO_Main = 1;
    private static final int GO_Login = 0;
    //延时时间
    private static final long DELAY_SECOND=1000;//1s
    private static final String SP_NAME = "firstIn_pref";

    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_Main:
                    Toast.makeText(SplashActivity.this, "主界面", Toast.LENGTH_SHORT).show();
                    break;
                case GO_Login:
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(intent);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    SplashActivity.this.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        SharedPreferences mpreference = getSharedPreferences(SP_NAME,MODE_PRIVATE);
        isLogin = mpreference.getBoolean("isLogin",false);
        SharedPreferences.Editor editor = mpreference.edit();
        editor.putBoolean("isLogin",false);
        editor.commit();

        if(isLogin){
            mHandler.sendEmptyMessageDelayed(GO_Main,DELAY_SECOND);
        }else {
            mHandler.sendEmptyMessageDelayed(GO_Login,DELAY_SECOND);
        }
    }
}
