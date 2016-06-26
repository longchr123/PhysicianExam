package com.example.asusk557.mytiku.fragment;
//更新习题数据库，比如添加表格，那么错题数据库也要改变，加密

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.activity.CreateActivity;
import com.example.asusk557.mytiku.activity.FragmentActivity;
import com.example.asusk557.mytiku.activity.Main2Activity;
import com.example.asusk557.mytiku.adapter.MyListViewAdapter;
import com.example.asusk557.mytiku.util.ConfigUtil;
import com.example.asusk557.mytiku.util.DownLoadSQLite;
import com.example.asusk557.mytiku.util.UseXiTiSQLite;
import com.umeng.analytics.MobclickAgent;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUSK557 on 2016/2/16.
 */
public class XiTiFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private Main2Activity main2Activity;
    private View view;
    private ListView lv;
    private MyListViewAdapter adapter;
    private List<String> list;
    private Intent intent;
    private TextView tv_upData;

    private InputStream inputStream;
    private URLConnection connection;
    private OutputStream outputStream;
    private Dialog dialog, dialog1, dialogAlert;
    private SQLiteDatabase db, database;
    private SharedPreferences sp;
    private boolean isLogin;//用于判断是否登录显示试用或者正式数据库
    private String number, password, DBurl;
    private RequestQueue mQueue;
    private StringRequest stringRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main2Activity = (Main2Activity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.from(main2Activity).inflate(R.layout.fragment_xiti, null);
        dialog1 = new AlertDialog.Builder(main2Activity).setTitle("加载中...").
                setView(new ProgressBar(main2Activity)).setCancelable(false).create();
        initShow();
        initView();
        getAlertData();
        initData();
        initListener();
        return view;
    }

    private void initShow() {
        sp = main2Activity.getSharedPreferences("test", Activity.MODE_PRIVATE);
        number = sp.getString("number", "");
        password = sp.getString("password", "");
        DBurl = sp.getString("FormalDBURL", "");
        if (number == null || number == "" || password == null || password == "") {
            isLogin = false;
        } else {
            isLogin = true;
        }
    }


    private void initData() {
        if (isLogin) {//正式
            database = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.normalFilename, ConfigUtil.mi_ma, null);
            if (new File(DownLoadSQLite.normalFilename).exists()) {
                list = UseXiTiSQLite.getTableChineseName(database);
            } else {
                list = new ArrayList<>();
                Toast.makeText(main2Activity, "暂无数据,请更新数据", Toast.LENGTH_SHORT).show();
            }
            tv_upData.setVisibility(view.VISIBLE);
        } else {//试用
            database = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.newFilename, ConfigUtil.mi_ma, null);
            list = UseXiTiSQLite.getTableChineseName(database);
            tv_upData.setVisibility(view.INVISIBLE);
        }
        adapter = new MyListViewAdapter(main2Activity, list, true);
        lv.setAdapter(adapter);
    }

    private void initListener() {
        lv.setOnItemClickListener(this);
        tv_upData.setOnClickListener(this);
    }

    private void initView() {
        lv = (ListView) view.findViewById(R.id.lv);
        tv_upData = (TextView) view.findViewById(R.id.tv_upData);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialog1.show();
        intent = new Intent(main2Activity, FragmentActivity.class);
        intent.putExtra("isXiTi", true);
        intent.putExtra("utilPosition", "util" + position);
        intent.putExtra("subject", list.get(position));
        intent.putExtra("tableName", position);
        main2Activity.startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        dialog1.dismiss();
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(main2Activity);
        builder.setMessage("确定更新数据吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!checkNetworkAvailable(main2Activity)) {
                    return;
                }
                showDialog("更新中...");
                new Thread() {
                    public void run() {
                        try {
                            DownFile(DBurl);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }.start();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //通过handler更改主线程
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 2://下载完成
                        if(!database.isOpen()){
                            Toast.makeText(main2Activity, "已经是最新的了", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            return;
                        }
                        Toast.makeText(main2Activity, "更新完成", Toast.LENGTH_LONG).show();
                        list = UseXiTiSQLite.getTableChineseName(database);
                        adapter.setData(list);
                        dialog.dismiss();
                        db = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.cuoTiFilename, ConfigUtil.mi_ma, null);
                        UseXiTiSQLite.creatTable(database, db);
                        break;
                    default:
                        break;
                }
            }
        }

    };

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
              /*
              * 向SD卡中写入文件,用Handle传递线程
              */
//        File f = new File(DownLoadSQLite.dirName);
//        if (!f.exists()) {
//            f.mkdir();
//        }
        try {
            outputStream = main2Activity.openFileOutput("ZYformal.db", Context.MODE_PRIVATE);
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

    public void showDialog(String title) {
        dialog = new AlertDialog.Builder(main2Activity).setTitle(title).
                setView(new ProgressBar(main2Activity)).setCancelable(false).show();
    }

    //检查网络状态
    public static boolean checkNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Toast.makeText(context, "请连接网络", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            Toast.makeText(context, "已连接wifi", Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            Toast.makeText(context, "正在使用2g/3g/4g网络", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                }
            }
        }
        Toast.makeText(context, "请连接网络", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void getAlertData() {
        if (!isLogin) {
            mQueue = Volley.newRequestQueue(main2Activity);
            String httpUrl = "http://www.zhongyuedu.com/api/registeralert.php";//公司服务器
            stringRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String resultCode = jsonObject.getString("resultCode");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        if (resultCode.equals("0")) {
                            String title = jsonObject1.getString("title");
                            String message = jsonObject1.getString("message");
                            String cancelButtonTitle = jsonObject1.getString("cancelButtonTitle");
                            String registerButtonTitle = jsonObject1.getString("registerButtonTitle");
                            dialogAlert = new AlertDialog.Builder(main2Activity).setTitle(title).setMessage(message)
                                    .setNegativeButton(cancelButtonTitle, null)
                                    .setPositiveButton(registerButtonTitle, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(main2Activity, CreateActivity.class);
                                            startActivity(intent);
                                            main2Activity.finish();
                                        }
                                    }).create();
                        } else {
//                            Toast.makeText(main2Activity, "", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(main2Activity, "网络连接有问题", Toast.LENGTH_SHORT).show();
                }
            });
            mQueue.add(stringRequest);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(db!=null){
            db.close();
        }
        if(database!=null){
            database.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialogAlert != null) {
            dialogAlert.show();
        }
        MobclickAgent.onPageStart("Main2Activity"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Main2Activity");
    }
}
