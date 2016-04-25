package com.example.ys.datashare.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ys.datashare.R;
import com.example.ys.datashare.activity.LoginActivity;
import com.example.ys.datashare.activity.MainActivity;
import com.example.ys.datashare.tool.SharedPreUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabDmy extends Fragment {

    private View view;
    private TextView signOut;
    private MainActivity main;


    public TabDmy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_d_my, container, false);
        main = (MainActivity) getActivity();
        initView();
        initEvent();
        return view;
    }

    private void initView() {
        signOut = (TextView)view.findViewById(R.id.signOut);

    }

    private void initEvent() {
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil sharePer = new SharedPreUtil("login");
                sharePer.deleParam(main);
                Intent intent = new Intent(main, LoginActivity.class);
                main.startActivity(intent);
                getActivity().finish();
            }
        });
    }

}
