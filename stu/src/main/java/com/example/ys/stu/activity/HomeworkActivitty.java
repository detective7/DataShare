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
import com.example.ys.stu.model.Homework;
import com.example.ys.stu.tool.SharedPreUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeworkActivitty extends Activity {

    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Response response;
    private static int REQUESTCODE = 100;

    private Intent intent;
    private TextView HWback, title, toClass, content, material, time, zuoYe;
    private Button hw_dl, tiJiao;
    private Homework hw;

    private Date date;
    private DateFormat sdf;
    private SharedPreUtil share = new SharedPreUtil("login");
    private String urlUpLode = Constant.MYURL + "tijiaozuoye.php";
    private String uploadFileName, uploadFilePath, timeSt;
    //从老师布置的作业上下载的资料
    private String urlDownload = Constant.MYURL + "xiazaiziliao.php";
    private File downloadFile;
    private String downloadFileName, downloadFilePath;
    private InputStream downloadInput;
    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(HomeworkActivitty.this, "提交失败", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(HomeworkActivitty.this, "提交成功", Toast.LENGTH_SHORT).show();
                //返回作业列表界面
                HomeworkActivitty.this.onBackPressed();
            }else if (msg.what == 10) {
                Toast.makeText(HomeworkActivitty.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
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
        tiJiao = (Button) this.findViewById(R.id.hw_tijiao);
        zuoYe = (TextView) this.findViewById(R.id.hwxx_zuoye);

        material = (TextView) this.findViewById(R.id.hwxx_material);
        if (hw.getMaterial() != null) {
            System.out.println(hw.getMaterial());
            String[] m1 = hw.getMaterial().split("/");
            material.setText(m1[3]);
            hw_dl.setClickable(false);

        } else {
            hw_dl.setClickable(false);
            material.setText("未上传相关资料");
        }
        //点击下载作业资料
        hw_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                okHttpClient = new OkHttpClient();
                //获取已发布作业列表
                FormEncodingBuilder builder = new FormEncodingBuilder();
                String[] m1 = hw.getMaterial().split("/");
                downloadFileName = m1[3];
                builder.add("mName", downloadFileName);
                request = new Request.Builder()
                        .url(urlDownload)
                        .post(builder.build())
                        .build();
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
//                            失败的，传入的文件会变大，打不开，待观察
//                            Headers responseHeaders = response.headers();
//                            for (int i = 0; i < responseHeaders.size(); i++) {
//                                Log.d("abc",responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                            }
////                            Log.d("abc", response.body().string());
//                            String dir=SDCard+"/stuData";
//                            new File(dir).mkdir();//新建文件夹
//                            downloadFile.createNewFile();//新建文件
//                            OutputStream output=new FileOutputStream(downloadFile);
//                            //读取大文件
//                            byte[] buffer=new byte[1024];
//                            //Log.d("abc",downloadFilePath);
//                            while(downloadInput.read(buffer)!=-1){
//                                output.write(buffer);
//                            }
//                            output.flush();
                        }
                    }
                });
            }
        });
        time = (TextView) this.findViewById(R.id.hwxx_time);
        time.setText(hw.getTime());
        HWback = (TextView) this.findViewById(R.id.HWBack);
        HWback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //跳转到FileFinder选择提交作业文件
        zuoYe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeworkActivitty.this, FileFinderActivity.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
        //提交作业
        tiJiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hw_id;
                String stu_id;
                hw_id=hw.getHw_id();
                stu_id = (String) share.getParam(HomeworkActivitty.this, "xuehao", "");
                //uploadFilePath
                if(uploadFilePath!=null&&uploadFileName!=null){
                    File file = new File(uploadFilePath);

                    date = new Date();
                    //format的格式可以任意
                    sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    timeSt = sdf.format(date);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    RequestBody requestBody = new MultipartBuilder()
                            .type(MultipartBuilder.FORM)
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data;name=\"stu_id\""),
                                    RequestBody.create(null, stu_id))
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data;name=\"hw_id\""),
                                    RequestBody.create(null, hw_id+""))
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data;name=\"time\""),
                                    RequestBody.create(null, timeSt))
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data; name=\"file\"; filename=\"" + uploadFileName + "\""), fileBody)
                            .build();

                    Request request = new Request.Builder()
                            .url(urlUpLode)
                            .post(requestBody)
                            .build();

                    okHttpClient = new OkHttpClient();

                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            mainHandler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            String resMsg = response.body().string();
                            Log.d("abc",resMsg);
                            if(resMsg.contains("success")){
                                mainHandler.sendEmptyMessage(1);
                            }else if(resMsg.contains("false")){
                                String[] sp1 = resMsg.split("Error:");
                                String[] sp2 = sp1[1].split(",");
                                Message msg = new Message();
                                msg.what=10;
                                msg.obj=sp2[0];
                                mainHandler.sendMessage(msg);
                            }
                        }
                    });
                }else{
                    Toast.makeText(HomeworkActivitty.this, "还未选择文件", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //选择提交作业文件返回文件名和路径
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HomeworkActivitty.this.REQUESTCODE && resultCode == HomeworkActivitty.this.RESULT_OK) {
            uploadFileName = data.getStringExtra("filename");
            uploadFilePath = data.getStringExtra("filepath");
            Toast.makeText(HomeworkActivitty.this, uploadFilePath, Toast.LENGTH_SHORT).show();
            zuoYe.setText(uploadFileName);
        }
    }

}
