package com.example.ys.datashare.view_subs;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ys.datashare.R;
import com.example.ys.datashare.activity.FileFinderActivity;
import com.example.ys.datashare.activity.MainActivity;
import com.example.ys.datashare.config.Constant;
import com.example.ys.datashare.tool.SharedPreUtil;
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
public class TabAnewwork extends Fragment {

    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Response response;
    private String[] allClass;

    private Spinner select_class;
    private EditText work_title, work_content;
    private TextView material;
    private Button upload;
    private String urlUpLode = Constant.MYURL + "uplode.php";
    private SharedPreUtil share = new SharedPreUtil("login");
    private View mainview;
    private String filename, filepath;

    private String tedId;
    private String toClass, title, content, time;
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
                System.out.println("error");
            } else if (msg.what == 1) {
                System.out.println("ok");
                Toast.makeText(Main, "新建成功", Toast.LENGTH_SHORT).show();
                select_class.setSelection(0);
                work_title.setText("");
                work_content.setText("");
                material.setText("上传资料小于8MB");
                filename = null;
                filepath = null;
            }else if (msg.what == 10) {
                Toast.makeText(Main, (String)msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private static int REQUESTCODE = 11;


    public TabAnewwork() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainview = inflater.inflate(R.layout.tab_a_newwork, container, false);

        initVIew();
        initEvent();
        //原本打算用请求获取，还是把它放在MainActivity，放SharePreference里
//        okHttpClient = new OkHttpClient();
//        args = new ArrayList<NameValuePair>();
//        Log.d("abc",args.toString());

//        //获取班级列表
//        FormEncodingBuilder builder = new FormEncodingBuilder();
//        builder.add("forwhat","class");
//
//        request = new Request.Builder()
//                .url(urlUpLode)
//                .post(builder.build())
//                .build();
//        okHttpClient.newCall(request).enqueue(new Callback(){
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                allClass = response.body().string().split(";");
//                //以下这段处理不能放在放回里面，逻辑错误
//                // 建立Adapter并且绑定数据源
//                ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, allClass);
//                //绑定 Adapter到控件
//                select_class.setAdapter(_Adapter);
//            }
//        });


        return mainview;
    }

    private void initVIew() {
        testThread = new MyThread("Handler Thread");
        testThread.start();
        select_class = (Spinner) mainview.findViewById(R.id.spinner);
        work_title = (EditText) mainview.findViewById(R.id.work_title);
        work_content = (EditText) mainview.findViewById(R.id.work_content);
        material = (TextView) mainview.findViewById(R.id.work_material);
        upload = (Button) mainview.findViewById(R.id.uplodeButton);
    }

    private void initEvent() {

        String allC = (String) share.getParam(getActivity(), "allClass", "");
        allClass = allC.split(";");
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, allClass);
        select_class.setAdapter(_Adapter);
        select_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toClass = (String) select_class.getSelectedItem();
                Log.d("abc", (String) select_class.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                tedId = (String) share.getParam(getActivity(), "xuehao", "");
                title = work_title.getText().toString();
                content = work_content.getText().toString();
                date = new Date();
                //format的格式可以任意

                sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
                time = sdf.format(date);
                if (toClass == null || toClass.equals("") || title == null || title.equals("") || content == null || content.equals("")) {
                    Toast.makeText(getActivity(), "信息不完整", Toast.LENGTH_SHORT).show();
                } else if (material.getText().toString().contains("上传资料小于8MB")) {
                    testThread.handler.sendEmptyMessage(2);
                } else {
                    File file = new File(filepath);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    RequestBody requestBody = new MultipartBuilder()
                            .type(MultipartBuilder.FORM)
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data;name=\"ted_id\""),
                                    RequestBody.create(null, tedId))
                            .addPart(Headers.of(
                                    "Content-Disposition",
                                    "form-data;name=\"toClass\""),
                                    RequestBody.create(null, toClass))
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
        if (requestCode == TabAnewwork.REQUESTCODE && resultCode == getActivity().RESULT_OK) {
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

                    new AlertDialog.Builder(getActivity())
                            .setTitle("确认")
                            .setMessage("确定不上传资料吗？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RequestBody requestBody = new MultipartBuilder()
                                            .type(MultipartBuilder.FORM)
                                            .addPart(Headers.of(
                                                    "Content-Disposition",
                                                    "form-data;name=\"ted_id\""),
                                                    RequestBody.create(null, tedId))
                                            .addPart(Headers.of(
                                                    "Content-Disposition",
                                                    "form-data;name=\"toClass\""),
                                                    RequestBody.create(null, toClass))
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
                                                    RequestBody.create(null, time)).build();
                                    Request request = new Request.Builder()
                                            .url(urlUpLode)
                                            .post(requestBody)
                                            .build();

                                    okHttpClient = new OkHttpClient();

                                    Call call = okHttpClient.newCall(request);
                                    call.enqueue(new com.squareup.okhttp.Callback() {
                                        @Override
                                        public void onFailure(Request request, IOException e) {
                                            mainHandler.sendEmptyMessage(0);
                                        }

                                        @Override
                                        public void onResponse(Response response) throws IOException {
                                            String resmsg = response.body().string();
                                            System.out.println(resmsg);
                                            if(resmsg.contains("success")){
                                                mainHandler.sendEmptyMessage(1);
                                            }else if(resmsg.contains("false")){
                                                mainHandler.sendEmptyMessage(10);
                                            }
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            };
            Looper.loop();
        }
    }
}
