package com.example.ys.stu.view_subs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ys.stu.R;
import com.example.ys.stu.config.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabAever extends Fragment {

    private View mainView;
    private static String urlYiFaBu = Constant.MYURL+"yifabu.php";



    public TabAever() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.tab_a_ever, container, false);

        return mainView;
    }

}
