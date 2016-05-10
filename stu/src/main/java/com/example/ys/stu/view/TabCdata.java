package com.example.ys.stu.view;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ys.stu.R;
import com.example.ys.stu.activity.MainActivity;
import com.example.ys.stu.view_subs.TabCever;
import com.example.ys.stu.view_subs.TabCnew;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabCdata extends Fragment {
    
    private MainActivity Main;
    private TextView ever,search,newData;
    private Fragment ever_fm,search_fm,newData_fm,now_fm;
    private View mainView;


    public TabCdata() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.tab_c_data, container, false);
        //初始化界面
        initView();
        //画面切换监听
        changeVIew();

        return mainView;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        Main = (MainActivity) getActivity();
        ever = (TextView) mainView.findViewById(R.id.mtr_ever);
//        search = (TextView) mainView.findViewById(R.id.mtr_search);
        newData = (TextView) mainView.findViewById(R.id.mtr_new);
        ever_fm = new TabCever();
//        search_fm = new TabCsearch();
        newData_fm = new TabCnew();
        ever.setBackground(Main.getResources().getDrawable(R.drawable.textview_border));
        // 默认首次填充已发布
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.data_context, ever_fm).commit();
        now_fm = ever_fm;
    }

    private void changeVIew() {
        ever.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                change(ever_fm);
                ever.setBackground(Main.getResources().getDrawable(R.drawable.textview_border));
                now_fm = ever_fm;
            }
        });
//        search.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onClick(View v) {
//                change(search_fm);
//                search.setBackground(Main.getResources().getDrawable(R.drawable.textview_border));
//                now_fm = search_fm;
//            }
//        });
        newData.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                change(newData_fm);
                newData.setBackground(Main.getResources().getDrawable(R.drawable.textview_border));
                now_fm = newData_fm;
            }
        });

    }

    @SuppressLint("NewApi")
    public void clean() {
        ever.setBackground(Main.getResources().getDrawable(R.drawable.textview_border2));
//        search.setBackground(Main.getResources().getDrawable(R.drawable.textview_border2));
        newData.setBackground(Main.getResources().getDrawable(R.drawable.textview_border2));
    }

    public void change(Fragment fm_next) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        clean();
        // now_fm.onPause();//不起作用，只有show和hide再起作用
        ft.hide(now_fm);
        if (fm_next.isAdded()) {
            // fm_next.onResume();
            ft.show(fm_next);
        } else {
            ft.replace(R.id.data_context, fm_next);
            // ft.addToBackStack(null); //加入回退栈，即按手机返回键时onPause返回原来状态
            ft.commit();
        }
    }

}
