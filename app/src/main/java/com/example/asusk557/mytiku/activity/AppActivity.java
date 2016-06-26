package com.example.asusk557.mytiku.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.adapter.MyAppAdapter;
import com.example.asusk557.mytiku.javaBean.AppItem;
import com.example.asusk557.mytiku.util.MyJson;

import java.util.ArrayList;
import java.util.List;

public class AppActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lv;
    private MyAppAdapter adapter;
    private List<AppItem> appItems;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        lv.setOnItemClickListener(this);
    }

    private void initData() {
        appItems = new ArrayList<>();
        adapter = new MyAppAdapter(this, appItems);
        lv.setAdapter(adapter);
        getDataFromNet();
    }

    private void getDataFromNet() {
        mQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest("http://www.zhongyuedu.com/api/tuijian.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        appItems = MyJson.parseJsonApp(response);
                        adapter.setData(appItems);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(request);
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("top", "应用推荐");
        intent.putExtra("title", "");
        intent.putExtra("webUrl", appItems.get(position).getAppUrl());
        startActivity(intent);
    }
}
