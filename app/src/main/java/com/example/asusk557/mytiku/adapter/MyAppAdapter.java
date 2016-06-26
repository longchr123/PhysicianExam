package com.example.asusk557.mytiku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.javaBean.AppItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public class MyAppAdapter extends BaseAdapter {
    private Context context;
    private List<AppItem> appItems;

    public MyAppAdapter(Context context, List<AppItem> appItems) {
        this.context = context;
        this.appItems = appItems;
    }

    @Override
    public int getCount() {
        return appItems.size();
    }

    public void setData(List<AppItem> appItems) {
        this.appItems = appItems;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private ImageView iv;
        private TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_applv, null);
            vh = new ViewHolder();
            vh.iv = (ImageView) convertView.findViewById(R.id.iv);
            vh.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(appItems.get(position).getImageUrl()).into(vh.iv);
        vh.tv.setText(appItems.get(position).getTitle());
        return convertView;
    }
}
