package com.example.ys.datashare.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ys.datashare.R;
import com.example.ys.datashare.config.Constant;
import com.example.ys.datashare.presenter.FragmentTabAdapter;
import com.example.ys.datashare.tool.JsonPost;
import com.example.ys.datashare.tool.SharedPreUtil;
import com.example.ys.datashare.view.TabAwork;
import com.example.ys.datashare.view.TabBmsg;
import com.example.ys.datashare.view.TabCdata;
import com.example.ys.datashare.view.TabDmy;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private RadioGroup rgs;
    private RadioButton radio;
    private RadioButton wode;
    public List<Fragment> fragments = new ArrayList<Fragment>();
    private ProgressDialog pDialog;
    private JsonPost jsonParser = new JsonPost();
    private SharedPreUtil user = new SharedPreUtil("login");
    private String xuehao, mima;
    private String urlGetIn = Constant.MYURL + "getin.php";
    private int statu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //类的实例会比其他语句早运行
        new CheckXinxi().execute();
        statu = (int) user.getParam(MainActivity.this, "statu", 4);
        Log.d("sbc",statu+"");

        if (statu == 1) {
            //添加四个fragment进list
            fragments.add(new TabAwork());
            fragments.add(new TabBmsg());
            fragments.add(new TabCdata());
            fragments.add(new TabDmy());
            rgs = (RadioGroup) findViewById(R.id.tabs_RG);
            radio = (RadioButton) findViewById(R.id.tab_rb_a);
            radio.setChecked(true);
            //再将list添加进Adapter
            FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, rgs);
            tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
                @Override
                public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {

                }
            });
        } else if(statu==2){
            Toast.makeText(MainActivity.this,"请选择学生版APP",Toast.LENGTH_LONG);
        }else {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            MainActivity.this.startActivity(intent);
            MainActivity.this.finish();
        }


    }

    /**
     * 查询账号相关信息
     */
    private class CheckXinxi extends AsyncTask<String, String, String> {

        private int success;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("正在登录");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            xuehao = (String) user.getParam(MainActivity.this, "xuehao", "");
            mima = (String) user.getParam(MainActivity.this, "mima", "");
            args.add(new BasicNameValuePair("user_num", xuehao));
            args.add(new BasicNameValuePair("password", mima));
            try {
                JSONObject json = jsonParser.makeHttpRequest(urlGetIn, "POST", args);
                String message = json.getString("message");
                success = json.getInt("success");
//                Log.d("successJson", json.getString("class") + "-->" + json.getString("statu") + "-->" + json.getString("department"));
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
            if (success == 1) {
            }
        }
    }

}
