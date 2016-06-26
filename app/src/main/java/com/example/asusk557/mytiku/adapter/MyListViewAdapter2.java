package com.example.asusk557.mytiku.adapter;
//点击其中一门考试，下载试用数据库，保存正式数据库下载链接和大小以及考试科目

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.activity.Main2Activity;
import com.example.asusk557.mytiku.javaBean.ExamItem;
import com.example.asusk557.mytiku.util.DownLoadSQLite;
import com.example.asusk557.mytiku.view.MyGridView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/2.
 */
public class MyListViewAdapter2 extends BaseAdapter {
    private Context context;
    private List<ExamItem> list;
    private URLConnection connection;
    private InputStream inputStream;
    private OutputStream outputStream;
    private SharedPreferences sp;
    private Dialog dialog;

    public MyListViewAdapter2(Context context, List<ExamItem> list) {
        this.context = context;
        this.list = list;
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
        TextView tv;
        MyGridView gv;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main_lv, null);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.tv_title);
            vh.gv = (MyGridView) convertView.findViewById(R.id.gv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv.setText(list.get(position).getContent());
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < list.get(position).getList().size(); i++) {
            stringList.add(list.get(position).getList().get(i).getTitle());
        }
        vh.gv.setAdapter(new MyGridViewAdapter(context, stringList));
        vh.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position2, long id) {
                new AlertDialog.Builder(context).setTitle("仅有一次选择机会，要谨慎哦！")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String url = list.get(position).getList().get(position2).getTestDBURL();
                        Log.i("url", url);
                        if (url == null || url.equals("")) {
                            Toast.makeText(context, "该类型暂时没有数据，请等待...", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            showDialog("加载中...");
                            sp = context.getSharedPreferences("test",
                                    Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("choose", list.get(position).getList().get(position2).getTitle());
                            editor.putString("FormalDBURL", list.get(position).getList().get(position2).getFormalDBURL());
                            editor.putString("formlDBSize", list.get(position).getList().get(position2).getFormalDBSize());
                            editor.putString("id", list.get(position).getList().get(position2).getId());
                            editor.commit();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    DownFile(url);
                                }
                            }).start();
                        }
                    }
                }).show();
            }
        });
        return convertView;
    }

    public void showDialog(String title) {
        dialog = new AlertDialog.Builder(context).setTitle(title).
                setView(new ProgressBar(context)).setCancelable(false).show();
    }

    //通过handler更改主线程
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 2://下载完成
                        dialog.dismiss();
                        Intent intent = new Intent(context, Main2Activity.class);
                        context.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        }

    };

    //下载数据库文件到本地
    private void DownFile(String urlString) {
                     /*
               * 连接到服务器
               */

        try {
            URL url = new URL(urlString);
            connection = url.openConnection();
            if (connection.getReadTimeout() == 5) {
                Log.i("---------->", "当前网络有问题");
                // return;
            }
            inputStream = connection.getInputStream();

        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            outputStream = context.openFileOutput("ZYtest.db", Context.MODE_PRIVATE);
            byte[] buffer = new byte[200];
            //开始读取
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            Message message2 = new Message();
            message2.what = 2;
            handler.sendMessage(message2);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
