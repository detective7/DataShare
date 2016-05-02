package com.example.ys.datashare.view_subs;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.ys.datashare.R;
import com.example.ys.datashare.activity.HomeworkActivitty;
import com.example.ys.datashare.config.Constant;
import com.example.ys.datashare.model.Homework;
import com.example.ys.datashare.presenter.HomeworkAdapter;
import com.example.ys.datashare.tool.SharedPreUtil;
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
public class TabAever extends ListFragment {

    private OkHttpClient okHttpClient;
    private Request request;

    private View mainView;
    private static String urlYiFaBu = Constant.MYURL + "yifabu.php";
    private SharedPreUtil share = new SharedPreUtil("login");
    private String tedId;

    private ListView hwListView;
    private HomeworkAdapter hwAdapter;
    private ArrayList<Homework> hwsFang;


    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                ArrayList<Homework> hws = (ArrayList<Homework>)msg.obj;
                hwsFang = new ArrayList<Homework>();
                for (int i=hws.size()-1;i>=0;i--){
                    hwsFang.add(hws.get(i));
                }
                hwAdapter = new HomeworkAdapter(getActivity(),hwsFang);
                hwListView.setAdapter(hwAdapter);
            }

        }
    };


    public TabAever() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.tab_a_ever, container, false);
        hwListView = (ListView) mainView.findViewById(R.id.hwListview);
        initView();
        initEvent();
        return mainView;
    }

    private void initView() {
        okHttpClient = new OkHttpClient();

        //获取已发布作业列表
        FormEncodingBuilder builder = new FormEncodingBuilder();
        tedId = (String) share.getParam(getActivity(), "xuehao", "");
        builder.add("user_num", tedId);

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
                String jasonHW = response.body().string();
                ArrayList<Homework> hw = JSON.parseObject(jasonHW, new TypeReference<ArrayList<Homework>>(){});
//                for(int i=0;i<hw.size();i++)
//                {
//                    System.out.println(hw.get(i));
//                }
                try {
                    //以防有时候数据没准备好久发送了消息
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what=1;
                msg.obj=hw;
                mainHandler.sendMessage(msg);

            }
        });

    }

    private void initEvent() {
        hwListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), HomeworkActivitty.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hw", hwsFang.get(position));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
//                if(hwsFang.get(position).getMaterial()!=null){
//                    System.out.println(hwsFang.get(position).getMaterial());
//
//                }else{
//                    System.out.println("该项无文件");
//                }

            }
        });
    }

    class myThread implements Runnable {

        @Override
        public void run() {

        }
    }


}
