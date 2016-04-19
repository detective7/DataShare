package com.example.ys.datashare.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ys.datashare.R;
import com.example.ys.datashare.presenter.FragmentTabAdapter;
import com.example.ys.datashare.view.TabAwork;
import com.example.ys.datashare.view.TabBmsg;
import com.example.ys.datashare.view.TabCdata;
import com.example.ys.datashare.view.TabDmy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private RadioGroup rgs;
    private RadioButton radio;
    private RadioButton wode;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //添加四个fragment进list
        fragments.add(new TabAwork());
        fragments.add(new TabBmsg());
        fragments.add(new TabCdata());
        fragments.add(new TabDmy());
        rgs = (RadioGroup) findViewById(R.id.tabs_RG);
        radio = (RadioButton) findViewById(R.id.tab_rb_a);
        radio.setChecked(true);
        //再将list添加进Adapter
        FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {

            }
        });

    }
}
