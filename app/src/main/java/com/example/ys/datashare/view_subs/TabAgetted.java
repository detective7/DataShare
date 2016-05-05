package com.example.ys.datashare.view_subs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ys.datashare.R;
import com.example.ys.datashare.config.Constant;
import com.example.ys.datashare.model.Homework;
import com.example.ys.datashare.presenter.HomeworkAdapter;
import com.example.ys.datashare.tool.SharedPreUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabAgetted extends Fragment {

    private OkHttpClient okHttpClient;
    private Request request;

    private View mainView;
    private static String urlYiFaBu = Constant.MYURL + "yifabu.php";
    private SharedPreUtil share = new SharedPreUtil("login");
    private String tedId;

    private ListView hwListView;
    private HomeworkAdapter hwAdapter;
    private ArrayList<Homework> hwsFang;

    public TabAgetted() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_a_getted, container, false);
    }

}
