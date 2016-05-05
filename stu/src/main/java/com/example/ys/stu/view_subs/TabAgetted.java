package com.example.ys.stu.view_subs;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.ys.stu.R;
import com.example.ys.stu.activity.HomeworkActivitty;
import com.example.ys.stu.config.Constant;
import com.example.ys.stu.model.Homework;
import com.example.ys.stu.presenter.HomeworkAdapter;
import com.example.ys.stu.tool.SharedPreUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabAgetted extends Fragment {

    private OkHttpClient okHttpClient;
    private Request request;

    private View mainView;
    private static String urlYiTiJiao = Constant.MYURL+"yitijiao.php";
    private SharedPreUtil share = new SharedPreUtil("login");
    private String stuId, stuClass;

    private ListView hwListView;
    private HomeworkAdapter hwAdapter;
    private ArrayList<Homework> hwsFang;

    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ArrayList<Homework> hws = (ArrayList<Homework>) msg.obj;
                hwsFang = new ArrayList<Homework>();
                for (int i = hws.size() - 1; i >= 0; i--) {
                    hwsFang.add(hws.get(i));
                }
                hwAdapter = new HomeworkAdapter(getActivity(), hwsFang);
                hwListView.setAdapter(hwAdapter);
            }

        }
    };

    public TabAgetted() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView=inflater.inflate(R.layout.tab_a_getted, container, false);
        hwListView = (ListView) mainView.findViewById(R.id.hw2_Listview);
        initView();
        return mainView;
    }

    private void initView() {
        okHttpClient = new OkHttpClient();

        //获取已发布作业列表
        FormEncodingBuilder builder = new FormEncodingBuilder();
        stuId = (String) share.getParam(getActivity(), "xuehao", "");
        stuClass = (String) share.getParam(getActivity(), "userClass", "");
        builder.add("user_num", stuId);
        builder.add("user_class", stuClass);

        request = new Request.Builder()
                .url(urlYiTiJiao)
                .post(builder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String jasonHW = response.body().string();
                //Log.d("abc",jasonHW+jasonHW.contains("hw_id"));
//                Log.d("abc",jasonHW+"");
                if (jasonHW.contains("hw_id")) {
                    ArrayList<Homework> hw = JSON.parseObject(jasonHW, new TypeReference<ArrayList<Homework>>() {
                    });
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = hw;
                    mainHandler.sendMessage(msg);
                }


            }
        });

        hwListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), HomeworkActivitty.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hw", hwsFang.get(position));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);

            }
        });
    }

}
