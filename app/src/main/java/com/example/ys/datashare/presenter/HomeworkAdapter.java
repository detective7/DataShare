package com.example.ys.datashare.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ys.datashare.R;
import com.example.ys.datashare.model.Homework;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Ys
 * 日期： 2016/4/30
 * 功能：
 */
public class HomeworkAdapter extends BaseAdapter {

    List<Homework> homeworks;
    LayoutInflater inflater;

    public HomeworkAdapter(Context context,ArrayList<Homework> homeworks){
        this.homeworks = homeworks;
        this.inflater = LayoutInflater.from(context);
    }

    //刷新页面
    public void onDateChange(ArrayList<Homework> homeworks){
        this.homeworks=homeworks;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return homeworks.size();
    }

    @Override
    public Object getItem(int position) {
        return homeworks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Homework homework = homeworks.get(position);
        ViewHolder holder;

        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_view,null);
            holder.item_title = (TextView)convertView.findViewById(R.id.item_title);
            holder.item_author = (TextView)convertView.findViewById(R.id.item_author);
            holder.item_time = (TextView)convertView.findViewById(R.id.item_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.item_title.setText("title："+homework.getTitle());
        holder.item_author.setText("toClass："+homework.getToClass());
        holder.item_time.setText(homework.getTime());
        return convertView;
    }

    class ViewHolder {
        TextView item_title;
        TextView item_author;
        TextView item_time;
    }
}
