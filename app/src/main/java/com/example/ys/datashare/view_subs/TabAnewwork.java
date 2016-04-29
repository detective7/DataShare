package com.example.ys.datashare.view_subs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.ys.datashare.R;
import com.example.ys.datashare.config.Constant;
import com.example.ys.datashare.tool.SharedPreUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
        select_class = (Spinner) mainview.findViewById(R.id.spinner);
        work_title = (EditText)mainview.findViewById(R.id.work_title);
        work_content = (EditText)mainview.findViewById(R.id.work_content);
        material = (TextView)mainview.findViewById(R.id.work_material);
    }

    private void initEvent() {

        String allC = (String)share.getParam(getActivity(),"allClass","");
        allClass = allC.split(";");
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, allClass);
        select_class.setAdapter(_Adapter);
        select_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("abc", (String)select_class.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
