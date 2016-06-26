package com.example.asusk557.mytiku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.javaBean.RealTime;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */
public class RealTimeAdapter extends BaseAdapter {
    private Context context;
    private List<RealTime> list;

    public RealTimeAdapter(Context context, List<RealTime> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void setData(List<RealTime> list){
        this.list=list;
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

    class ViewHolder {
        private ImageView iv;
        private TextView tv_title, tv_teacher, tv_time, tv_person;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_realtime_lv, null);
            vh = new ViewHolder();
            vh.iv = (ImageView) convertView.findViewById(R.id.iv);
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.tv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.tv_person = (TextView) convertView.findViewById(R.id.tv_person);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_title.setText(list.get(position).getTitle());
        vh.tv_teacher.setText("主讲："+list.get(position).getTeacher());
        vh.tv_time.setText("时间："+list.get(position).getDate());
        vh.tv_person.setText(list.get(position).getNum());
        Picasso.with(context).load(list.get(position).getPic()).into(vh.iv);
        return convertView;
    }
}
