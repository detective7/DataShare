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
import com.example.ys.stu.view_subs.TabAever;
import com.example.ys.stu.view_subs.TabAgetted;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabAwork extends Fragment {

    private MainActivity Main;
    private TextView never, getted;
    private Fragment never_fm, getted_fm,now_fm;
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
        never = (TextView) mainView.findViewById(R.id.title_never);
        getted = (TextView) mainView.findViewById(R.id.title_getted);
        never_fm = new TabAever();
        getted_fm = new TabAgetted();
        never.setBackground(Main.getResources().getDrawable(R.drawable.textview_border));
        // 默认首次填充已发布
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.work_context, never_fm).commit();
        now_fm = never_fm;
    }

    private void changeVIew() {
        never.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                change(never_fm);
                never.setBackground(Main.getResources().getDrawable(R.drawable.textview_border));
                now_fm = never_fm;
            }
        });
        getted.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                change(getted_fm);
                getted.setBackground(Main.getResources().getDrawable(R.drawable.textview_border));
                now_fm = getted_fm;
            }
        });

    }

    @SuppressLint("NewApi")
    public void clean() {
        never.setBackground(Main.getResources().getDrawable(R.drawable.textview_border2));
        getted.setBackground(Main.getResources().getDrawable(R.drawable.textview_border2));
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
