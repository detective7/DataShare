package com.example.ys.stu.view_subs;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.ys.stu.R;
import com.example.ys.stu.activity.DataActivity;
import com.example.ys.stu.config.Constant;
import com.example.ys.stu.model.Data;
import com.example.ys.stu.presenter.DataAdapter;
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
public class TabCever extends ListFragment {

    private OkHttpClient okHttpClient;
    private Request request;

    private View mainView;
    private static String urlYiFaBu = Constant.MYURL + "shared.php";

    private ListView dataListView;
    private DataAdapter dataAdapter;
    private ArrayList<Data> datasFang;


    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                ArrayList<Data> datas = (ArrayList<Data>)msg.obj;
                datasFang = new ArrayList<Data>();
                for (int i=datas.size()-1;i>=0;i--){
                    datasFang.add(datas.get(i));
                }
                dataAdapter = new DataAdapter(getActivity(),datasFang);
                dataListView.setAdapter(dataAdapter);
            }

        }
    };


    public TabCever() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.tab_c_ever, container, false);
        dataListView = (ListView) mainView.findViewById(R.id.dataEverLV);
        initView();
        initEvent();
        return mainView;
    }

    private void initView() {
        okHttpClient = new OkHttpClient();

        //获取已发布作业列表
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("1","1");

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
                Log.d("mydata",jasonHW);
                ArrayList<Data> datas = JSON.parseObject(jasonHW, new TypeReference<ArrayList<Data>>(){});
                for(int i=0;i<datas.size();i++)
                {
//                    Log.d("mydata",datas.get(i).getTime());
                }
                try {
                    //以防有时候数据没准备好久发送了消息
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what=1;
                msg.obj=datas;
                mainHandler.sendMessage(msg);

            }
        });

    }

    private void initEvent() {
        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", datasFang.get(position));
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


}
