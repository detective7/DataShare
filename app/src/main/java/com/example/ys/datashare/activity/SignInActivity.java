package com.example.ys.datashare.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ys.datashare.R;
import com.example.ys.datashare.tool.JsonPost;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends Activity {

    private TextView zhuCeBack, faSongYanZheng;
    private EditText zhuCeXueHao, zhuCeMiMa, zhuCeMiMa2, zhuCeShouJi, yanZhengMa;
    private Button zhuCe;
    private static String urlZhuCe = "http://192.168.88.101/testShare/signIn.php";
    private ProgressDialog pDialog;
    String userNum, password,password_copy, phone;
    JsonPost jsonParser = new JsonPost();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        zhuCeBack = (TextView) this.findViewById(R.id.zhuCeBack);
        faSongYanZheng = (TextView) this.findViewById(R.id.faSongYanZheng);
        zhuCeXueHao = (EditText) this.findViewById(R.id.zhuCeXueHao);
        zhuCeMiMa = (EditText) this.findViewById(R.id.zhuCeMiMa);
        zhuCeMiMa2 = (EditText) this.findViewById(R.id.zhuCeMiMa2);
        zhuCeShouJi = (EditText) this.findViewById(R.id.zhuCeShouJi);
        yanZhengMa = (EditText) this.findViewById(R.id.yanZhengMa);

        zhuCe = (Button) this.findViewById(R.id.zhuCe);

        zhuCeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        zhuCe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNum = zhuCeXueHao.getText().toString();
                password = zhuCeMiMa.getText().toString();
                phone = zhuCeShouJi.getText().toString();
                if (dataTrue()) {
                    new Send().execute();
                }
            }
        });

        zhuCeXueHao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                userNum = zhuCeXueHao.getText().toString();
                if (hasFocus) {

                } else if ("".equals(userNum) || userNum == null) {
                    Toast.makeText(SignInActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
                } else {
                    //To do 验证学号是否存在
                }

            }
        });

        zhuCeMiMa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password = zhuCeMiMa.getText().toString();
                if(hasFocus){

                }else if("".equals(password) || password == null){
                    Toast.makeText(SignInActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
            }
        });

        zhuCeMiMa2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password_copy = zhuCeMiMa2.getText().toString();
                if(hasFocus){

                }else if("".equals(password_copy) || password_copy == null){
                    Toast.makeText(SignInActivity.this, "请再次密码", Toast.LENGTH_SHORT).show();
                }else if(!password_copy.equals(password)){
                    Toast.makeText(SignInActivity.this, "再次密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        faSongYanZheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 验证码
            }
        });


    }

    /**
     * @return 判断注册时输入数据的正确性
     */
    private boolean dataTrue() {
        return true;
    }

    private class Send extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage("正在注册");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("user_num", userNum));
            args.add(new BasicNameValuePair("password", password));
            args.add(new BasicNameValuePair("phone", phone));
            try {
                JSONObject json = jsonParser.makeHttpRequest(urlZhuCe, "POST", args);
                String message = json.getString("message");
                return message;
            } catch (Exception e) {
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
        }
    }
}
