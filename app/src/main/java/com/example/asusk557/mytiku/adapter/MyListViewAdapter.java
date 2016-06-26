package com.example.asusk557.mytiku.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asusk557.mytiku.R;

import java.util.List;

/**
 * Created by Administrator on 2016/2/24.
 */
public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private boolean isXiTi;

    public MyListViewAdapter(Context context, List<String> list,boolean isXiTi) {
        this.context = context;
        this.list = list;
        this.isXiTi=isXiTi;
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

    class ViewHolder {
        private TextView tv_subject;
    }

    public void setData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mylistview, null);
            vh = new ViewHolder();
            vh.tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_subject.setText(list.get(position));
        if(!isXiTi){
            vh.tv_subject.setTextColor(Color.RED);
        }
        return convertView;
    }
}
