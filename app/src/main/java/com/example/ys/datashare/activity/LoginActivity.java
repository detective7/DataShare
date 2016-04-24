package com.example.ys.datashare.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ys.datashare.R;
import com.example.ys.datashare.config.Constant;
import com.example.ys.datashare.tool.JsonPost;
import com.example.ys.datashare.tool.SharedPreUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    private ImageView canSee;
    private EditText xueHao, miMa;
    private TextView zhuCe, wangJiMiMa;
    private Boolean isCanSee = false;
    private String xuehao, mima;
    private Button login_bt;
    private ProgressDialog pDialog;
    JsonPost jsonParser = new JsonPost();
    private static String urlDenglu = Constant.MYURL+"login.php";
    Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        canSee = (ImageView) this.findViewById(R.id.canSee);
        xueHao = (EditText) this.findViewById(R.id.xueHao);
        miMa = (EditText) this.findViewById(R.id.miMa);
        zhuCe = (TextView) this.findViewById(R.id.zhuCe);
        wangJiMiMa = (TextView) this.findViewById(R.id.wangJiMiMa);
        login_bt = (Button) this.findViewById(R.id.login);

        canSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCanSee) {
                    //如果亮，显示密码
                    isCanSee = true;
                    miMa.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    canSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.eye_liang));
                } else {
                    //否则隐藏密码
                    isCanSee = false;
                    miMa.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    canSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.eye_an));
                }
            }
        });

        zhuCe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                LoginActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        xueHao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                xuehao = xueHao.getText().toString().trim();
                if (hasFocus) {

                } else if ("".equals(xuehao) || xuehao == null) {
                    Toast.makeText(LoginActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
                }

            }
        });

        miMa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mima = miMa.getText().toString().trim();
                if (hasFocus) {

                } else if ("".equals(mima) || mima == null) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }

            }
        });

        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuehao = xueHao.getText().toString().trim();
                mima = miMa.getText().toString().trim();
                if ("".equals(xuehao) || xuehao == null) {
                    Toast.makeText(LoginActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
                } else if ("".equals(mima) || mima == null) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }else{
                    new Login().execute();
//                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                    LoginActivity.this.startActivity(intent);
                }

            }
        });
    }

    private class Login extends AsyncTask<String, String, String> {

        private int success;
        private int statu;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("正在登录");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("user_num", xuehao));
            args.add(new BasicNameValuePair("password", mima));
            try {
                JSONObject json = jsonParser.makeHttpRequest(urlDenglu, "POST",args);
                String message = json.getString("message");
                success = json.getInt("success");
                statu = json.getInt("statu");
                Log.d("success",success+"");
                return message;
            }catch(Exception e){
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            //doInBackground返回值-->s
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            if(success==1){
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                SharedPreUtil LoginSha = new SharedPreUtil("login");
                LoginSha.setParam(LoginActivity.this,"xuehao",xuehao);
                LoginSha.setParam(LoginActivity.this,"mima",mima);
                LoginSha.setParam(LoginActivity.this,"statu",statu);
                Log.d("abc",xuehao+"   "+mima);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
        }
    }

}
