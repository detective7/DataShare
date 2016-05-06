package com.example.ys.datashare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ys.datashare.R;
import com.example.ys.datashare.config.Constant;
import com.example.ys.datashare.model.Homework;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class HomeworkActivitty extends Activity {

    private Intent intent;
    private TextView HWback, title, toClass, content, material, time, tongji;
    private Button hw_dl, tiJiaoXiangxi, hw_download;
    private Homework hw;
    private OkHttpClient okHttpClient;
    private Request request;
    private static String urlSubmitStatistics = Constant.MYURL + "tijiaotongji.php";

    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                tongji.setText((String)msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        intent = getIntent();
        hw = (Homework) intent.getSerializableExtra("hw");
        initView();
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.hwxx_title);
        title.setText("作业标题：" + hw.getTitle());
        toClass = (TextView) this.findViewById(R.id.hwxx_class);
        toClass.setText("发送班级：" + hw.getToClass());
        content = (TextView) this.findViewById(R.id.hwxx_content);
        content.setText("作业内容：\n" + hw.getContent());
        hw_dl = (Button) this.findViewById(R.id.hw_download);

        material = (TextView) this.findViewById(R.id.hwxx_material);
        if (hw.getMaterial() != null) {
            System.out.println(hw.getMaterial());
            String[] m1 = hw.getMaterial().split("/");
            material.setText("包含文件：" + m1[3]);
            //这里是教师上传的资料，所以不用下载
            hw_dl.setClickable(false);
            hw_dl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            hw_dl.setClickable(false);
            material.setText("包含文件：未上传相关资料");
        }

        time = (TextView) this.findViewById(R.id.hwxx_time);
        time.setText(hw.getTime());
        HWback = (TextView) this.findViewById(R.id.HWBack);
        HWback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tongji = (TextView)this.findViewById(R.id.tongJi);
        tiJiaoXiangxi = (Button)this.findViewById(R.id.tiJiaoXiangXi);
        hw_download = (Button)this.findViewById(R.id.hw_download);


        //获取已发布作业列表
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("hw_id", hw.getHw_id() + "");

        request = new Request.Builder()
                .url(urlSubmitStatistics)
                .post(builder.build())
                .build();
        okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String jasonHW = response.body().string();
                Log.d("abc", jasonHW);
                if (jasonHW.contains("/")) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = jasonHW;
                    mainHandler.sendMessage(msg);
                }
            }
        });
    }
}
