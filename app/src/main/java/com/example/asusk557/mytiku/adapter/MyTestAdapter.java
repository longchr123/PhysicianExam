package com.example.asusk557.mytiku.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asusk557.mytiku.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ASUSK557 on 2016/2/17.
 */
public class MyTestAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private HashMap<Integer, Integer> hashMap = new HashMap<>();

    public MyTestAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        for (int i = 0; i < list.size(); i++) {
            hashMap.put(i, 0);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void changeTextColor(int position) {
        hashMap.clear();
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                hashMap.put(i, 1);
            } else {
                hashMap.put(i, 0);
            }
        }
        notifyDataSetChanged();
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
        private TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fragment_lv, null);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv.setText(list.get(position));
        if (hashMap.get(position) == 1) {
            vh.tv.setTextColor(Color.RED);
        } else {
            vh.tv.setTextColor(Color.BLACK);
        }
        return convertView;
    }
}
