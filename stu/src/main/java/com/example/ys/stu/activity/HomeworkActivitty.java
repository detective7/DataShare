package com.example.ys.stu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ys.stu.R;
import com.example.ys.stu.model.Homework;


public class HomeworkActivitty extends Activity {

    private Intent intent;
    private TextView HWback, title, toClass, content, material, time;
    private Button hw_dl;
    private Homework hw;

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
        hw_dl=(Button)this.findViewById(R.id.hw_download);

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

    }
}
