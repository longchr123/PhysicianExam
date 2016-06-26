package com.example.asusk557.mytiku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asusk557.mytiku.R;

import java.util.List;

/**
 * Created by ASUSK557 on 2016/2/16.
 */
public class MyGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String>list;

    public MyGridViewAdapter(Context context, List<String>list) {
        this.context = context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv, null);
            tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(tv);
        } else {
            tv = (TextView) convertView.getTag();
        }
        tv.setText(list.get(position));
        return convertView;
    }
}


