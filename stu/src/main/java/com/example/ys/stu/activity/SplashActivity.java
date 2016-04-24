package com.example.ys.stu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.ys.stu.R;
import com.example.ys.stu.tool.SharedPreUtil;


/**
 *
 *
 *
 */
public class SplashActivity extends Activity {

    private SharedPreUtil isLogin = new SharedPreUtil("login");
    private static final int GO_Main = 1;
    private static final int GO_Login = 0;
    //延时时间
    private static final long DELAY_SECOND=1000;//1s

    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_Main:
                    Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(intent1);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    SplashActivity.this.finish();
                    break;
                case GO_Login:
                    Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(intent2);
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
        String xuehao = (String)isLogin.getParam(SplashActivity.this,"xuehao","");
        if(!xuehao.equals("")){
            mHandler.sendEmptyMessageDelayed(GO_Main,DELAY_SECOND);
        }else {
            mHandler.sendEmptyMessageDelayed(GO_Login,DELAY_SECOND);
        }
    }
}
