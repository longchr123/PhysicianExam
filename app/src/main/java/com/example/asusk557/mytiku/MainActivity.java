package com.example.asusk557.mytiku;
//下载试用数据库，如果数据库文件已经存在则不再下载

import android.app.Activity;
import android.content.Context;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asusk557.mytiku.adapter.MyListViewAdapter2;
import com.example.asusk557.mytiku.javaBean.ExamItem;
import com.example.asusk557.mytiku.util.MyJson;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity {

    private String json;
    private ListView lv;
    private List<ExamItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkNetworkAvailable(this)) {
            initView();
        } else {
            return;
        }
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        down();
    }

    //下载json字符串
    private void down() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest("http://www.zhongyuedu.com/api/tk_examtype_encrypted.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        json = response;
                        list = MyJson.parseJson(json);
                        MyListViewAdapter2 adapter = new MyListViewAdapter2(MainActivity.this, list);
                        lv.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);
    }


    //检查网络状态
    public static boolean checkNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Toast.makeText(context, "初次进入本应用，请连接网络", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(context, "初次进入本应用，请连接网络", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
