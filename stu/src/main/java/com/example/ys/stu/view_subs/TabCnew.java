package com.example.ys.stu.view_subs;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ys.stu.R;
import com.example.ys.stu.activity.FileFinderActivity;
import com.example.ys.stu.activity.MainActivity;
import com.example.ys.stu.config.Constant;
import com.example.ys.stu.tool.SharedPreUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabCnew extends Fragment {

    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Response response;
    private String[] allClass;

    private EditText data_title, data_content;
    private TextView material;
    private Button upload;
    private String urlUpLode = Constant.MYURL + "uplode_data.php";
    private SharedPreUtil share = new SharedPreUtil("login");
    private View mainview;
    private String filename, filepath;

    private String userId;
    private String  title, content, time;
    Date date;
    DateFormat sdf;
    MyThread testThread;
    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final MainActivity Main = (MainActivity) getActivity();
            if (msg.what == 0) {
                Toast.makeText(Main, "新建失败", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(Main, "新建成功", Toast.LENGTH_SHORT).show();
                data_title.setText("");
                data_content.setText("");
                material.setText("上传资料小于8MB");
                filename = null;
                filepath = null;
            }else if (msg.what == 10) {
                Toast.makeText(Main, (String)msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private static int REQUESTCODE = 11;


    public TabCnew() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.tab_c_newdata, container, false);

        initVIew();
        initEvent();

        return mainview;
    }

    private void initVIew() {
        testThread = new MyThread("Handler Thread");
        testThread.start();
        data_title = (EditText) mainview.findViewById(R.id.data_title);
        data_content = (EditText) mainview.findViewById(R.id.data_content);
        material = (TextView) mainview.findViewById(R.id.data_material);
        upload = (Button) mainview.findViewById(R.id.uplodeShare);
    }

    private void initEvent() {

        String allC = (String) share.getParam(getActivity(), "allClass", "");
        allClass = allC.split(";");
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, allClass);
        material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FileFinderActivity.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = (String) share.getParam(getActivity(), "xuehao", "");
                title = data_title.getText().toString();
                content = data_content.getText().toString();
                date = new Date();
                //format的格式可以任意

                sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                time = sdf.format(date);
                if (title == null || title.equals("") || content == null || content.equals("")) {
                    Toast.makeText(getActivity(), "信息不完整", Toast.LENGTH_SHORT).show();
                } else if (material.getText().toString().contains("上传资料小于8MB")) {
                    Toast.makeText(getActivity(), "请选择要上传的资料", Toast.LENGTH_SHORT).show();
                } else {
                    File file = new File(filepath);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    RequestBody requestBody = new MultipartBuilder()
                            .type(MultipartBuilder.FORM)
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data;name=\"user_id\""),
                                    RequestBody.create(null, userId))
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data;name=\"title\""),
                                    RequestBody.create(null, title))
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data;name=\"content\""),
                                    RequestBody.create(null, content))
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data;name=\"time\""),
                                    RequestBody.create(null, time))
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data; name=\"file\"; filename=\"" + filename + "\""), fileBody)
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
//                        Headers responseHeaders = response.headers();
//                        for (int i = 0; i < responseHeaders.size(); i++) {
//                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                        }
                            String resMsg = response.body().string();
                            System.out.println(resMsg);
                            if(resMsg.contains("success")){
                                mainHandler.sendEmptyMessage(1);
                            }else if(resMsg.contains("false")){
                                String[] sp1 = resMsg.split(":");
                                String[] sp2 = sp1[1].split(",");
                                Message msg = new Message();
                                msg.what=10;
                                msg.obj=sp2[0];
                                mainHandler.sendMessage(msg);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TabCnew.REQUESTCODE && resultCode == getActivity().RESULT_OK) {
            filename = data.getStringExtra("filename");
            filepath = data.getStringExtra("filepath");
            Toast.makeText(getActivity(), filepath, Toast.LENGTH_SHORT).show();
            material.setText(filename);
        }
    }

    class MyThread extends HandlerThread {

        private Handler handler;

        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                }
            };
            Looper.loop();
        }
    }
}
