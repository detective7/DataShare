package com.example.ys.datashare.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ys.datashare.R;
import com.example.ys.datashare.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Ys
 * 日期： 2016/5/17
 * 功能：
 */
public class MsgAdapter extends BaseAdapter {

    List<Message> messages;
    LayoutInflater inflater;

    public MsgAdapter(Context context, ArrayList<Message> messages){
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message msg = messages.get(position);
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.msg_item,null);
            holder.item_sender = (TextView)convertView.findViewById(R.id.msg_sUser);
            holder.item_time = (TextView)convertView.findViewById(R.id.msg_time);
            holder.item_cont = (TextView)convertView.findViewById(R.id.msg_nMsg);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.item_sender.setText(msg.getUser_s());
        holder.item_time.setText(msg.getTime());
        holder.item_cont.setText(msg.getText());
        return convertView;
    }

    private class ViewHolder {
        TextView item_sender;
        TextView item_time;
        TextView item_cont;
    }
}
