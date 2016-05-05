package com.example.ys.datashare.view;


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

import com.example.ys.datashare.R;
import com.example.ys.datashare.activity.MainActivity;
import com.example.ys.datashare.view_subs.TabAever;
import com.example.ys.datashare.view_subs.TabAnewwork;

//import com.example.ys.datashare.view_subs.TabAgetted;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabAwork extends Fragment {

    private MainActivity Main;
    private TextView ever,  newWork;
    private Fragment ever_fm, newWork_fm,now_fm;
    private View mainView;

    public TabAwork() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.tab_a_work, container, false);
        //初始化界面
        initView();
        //画面切换监听
        changeVIew();

        return mainView;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        Main = (MainActivity) getActivity();
        ever = (TextView) mainView.findViewById(R.id.title_ever);
//        getted = (TextView) mainView.findViewById(R.id.title_getted);
        newWork = (TextView) mainView.findViewById(R.id.title_newwork);
        ever_fm = new TabAever();
//        getted_fm = new TabAgetted();
        newWork_fm = new TabAnewwork();
        ever.setBackground(Main.getResources().getDrawable(R.drawable.textview_border));
        // 默认首次填充已发布
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.work_context, ever_fm).commit();
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
//        getted.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onClick(View v) {
//                change(getted_fm);
//                getted.setBackground(Main.getResources().getDrawable(R.drawable.textview_border));
//                now_fm = getted_fm;
//            }
//        });
        newWork.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                change(newWork_fm);
                newWork.setBackground(Main.getResources().getDrawable(R.drawable.textview_border));
                now_fm = newWork_fm;
            }
        });

    }

    @SuppressLint("NewApi")
    public void clean() {
        ever.setBackground(Main.getResources().getDrawable(R.drawable.textview_border2));
//        getted.setBackground(Main.getResources().getDrawable(R.drawable.textview_border2));
        newWork.setBackground(Main.getResources().getDrawable(R.drawable.textview_border2));
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
            ft.replace(R.id.work_context, fm_next);
            // ft.addToBackStack(null); //加入回退栈，即按手机返回键时onPause返回原来状态
            ft.commit();
        }
    }


}
