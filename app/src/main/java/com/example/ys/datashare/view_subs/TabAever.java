package com.example.ys.datashare.view_subs;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.ys.datashare.R;
import com.example.ys.datashare.config.Constant;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabAever extends Fragment {

    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private Response response;
    private String[] allClass;

    private View mainView;
    private static String urlYiFaBu = Constant.MYURL + "yifabu.php";
    private Message message;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };


    public TabAever() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.tab_a_ever, container, false);
        initView();
        initEvent();
        return mainView;
    }

    private void initView() {
        okHttpClient = new OkHttpClient();

        //获取班级列表
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("forwhat", "class");

        request = new Request.Builder()
                .url(urlYiFaBu)
                .post(builder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                allClass = response.body().string().split(";");
                //以下这段处理不能放在放回里面，逻辑错误
                // 建立Adapter并且绑定数据源
                ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, allClass);
                //绑定 Adapter到控件
//                select_class.setAdapter(_Adapter);
            }
        });

    }

    private void initEvent() {
    }

    class myThread implements Runnable {

        @Override
        public void run() {
//            okHttpClient = new OkHttpClient();
//
//            FormEncodingBuilder builder = new FormEncodingBuilder();
//            builder.add("forwhat", "class");
//
//            request = new Request.Builder()
//                    .url(urlYiFaBu)
//                    .post(builder.build())
//                    .build();
//            okHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    allClass = response.body().string().split(";");
//                    //以下这段处理不能放在放回里面，逻辑错误
//                    // 建立Adapter并且绑定数据源
//                    HomeworkAdapter _Adapter = new HomeworkAdapter(getActivity(),);
//                    //绑定 Adapter到控件
//                }
//            });

        }
    }


}
