package com.example.ys.stu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ys.stu.R;
import com.example.ys.stu.config.Constant;
import com.example.ys.stu.model.Data;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataActivity extends Activity {

    private Intent intent;
    private TextView databack, title, author, content, material, time;
    private Button data_dl, data_download;
    private Data data;
    private OkHttpClient okHttpClient;
    private Request request;
    private String urlDL = Constant.MYURL + "dlshare.php";
    private File downloadFile;
    private String downloadFileName, downloadFilePath;
    private InputStream downloadInput;
    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 11) {
                Toast.makeText(DataActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        intent = getIntent();
        data = (Data) intent.getSerializableExtra("data");
        initView();
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.dataxx_title);
        title.setText("资料标题：" + data.getTitle());
        author = (TextView) this.findViewById(R.id.dataxx_author);
        author.setText("发送作者：" + data.getUserId());
        content = (TextView) this.findViewById(R.id.dataxx_content);
        content.setText("资料内容：\n" + data.getContent());
        data_dl = (Button) this.findViewById(R.id.data_download);

        material = (TextView) this.findViewById(R.id.dataxx_material);
        System.out.println(data.getmPath());
        String[] m1 = data.getmPath().split("/");
        material.setText("包含文件：" + m1[3]);
        data_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        time = (TextView) this.findViewById(R.id.dataxx_time);
        time.setText(data.getmTime());
        databack = (TextView) this.findViewById(R.id.dataBack);
        databack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        data_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormEncodingBuilder builder = new FormEncodingBuilder();
                String[] m1 = data.getmPath().split("/");
                downloadFileName = m1[3];
                builder.add("mName", downloadFileName);

                request = new Request.Builder()
                        .url(urlDL)
                        .post(builder.build())
                        .build();
                okHttpClient = new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        /**
                         * 取得SDCard的路径： Environment.getExternalStorageDirectory()
                         * 检查要保存的文件上是否已经存在
                         * 不存在，新建文件夹，新建文件
                         * 将input流中的信息写入SDCard
                         */
                        String SDCard = Environment.getExternalStorageDirectory() + "";
                        downloadFilePath = SDCard + "/stuData/" + downloadFileName;//文件存储路径
//                        downloadFilePath = SDCard + "/stuData/testPHP.txt";
                        downloadFile = new File(downloadFilePath);
                        downloadInput = response.body().byteStream();
                        if (downloadFile.exists()) {
                            Log.d("abc", "phone file exits");
                            return;
                        } else {
                            byte[] buf = new byte[2048];
                            int len = 0;
                            FileOutputStream fos = null;
                            try {
                                final long total = response.body().contentLength();
                                Log.i("abc", "total--" + total);
                                long sum = 0;
                                File dir = new File(SDCard + "/stuData/");
                                if (!dir.exists()) dir.mkdirs();
                                fos = new FileOutputStream(downloadFile);
                                while ((len = downloadInput.read(buf)) != -1) {
                                    sum += len;
                                    fos.write(buf, 0, len);
                                    final long finalSum = sum;
//                                    han.obtainMessage(PROG, String.valueOf(finalSum * 1.0f / total)).sendToTarget();
                                }
                                fos.flush();
                                mainHandler.sendEmptyMessage(11);
                            } finally {
                                try {
                                    if (downloadInput != null) downloadInput.close();
                                } catch (IOException e) {
                                }
                                try {
                                    if (fos != null) fos.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                    }
                });
            }
        });
    }

}