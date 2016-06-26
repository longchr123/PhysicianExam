package com.example.asusk557.mytiku.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.util.ConfigUtil;
import com.example.asusk557.mytiku.util.DownLoadSQLite;
import com.example.asusk557.mytiku.util.UseXiTiSQLite;
import com.umeng.analytics.MobclickAgent;


import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button tv_login;
    private TextView tv_create;
    private EditText et_phone, et_mima;
    private StringRequest stringRequest;
    private RequestQueue mQueue;
    private URLConnection connection;
    private InputStream inputStream;
    private OutputStream outputStream;
    private SharedPreferences sp;
    private SQLiteDatabase db, database;
    private Dialog dialog;
    private boolean isChoose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initListener();
    }

    private void initData() {

    }

    private void initListener() {
        tv_login.setOnClickListener(this);
        tv_create.setOnClickListener(this);
    }

    private void initView() {
        tv_login = (Button) findViewById(R.id.tv_login);
        tv_create = (TextView) findViewById(R.id.tv_create);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_mima = (EditText) findViewById(R.id.et_mima);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                if (!isChoose) {
                    login();
                    isChoose = true;
                }
                break;
            case R.id.tv_create:
                Intent intent = new Intent(this, CreateActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    //登录逻辑
    private void login() {
        mQueue = Volley.newRequestQueue(this);
        String httpUrl = "http://www.zhongyuedu.com/api/login.php";//公司服务器
        stringRequest = new StringRequest(Request.Method.POST, httpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("mmm", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String resultCode = jsonObject.getString("resultCode");
                    String result = jsonObject.getString("result");
                    if (resultCode.equals("0")) {
                        MobclickAgent.onProfileSignIn("userID");
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        dialog = new AlertDialog.Builder(LoginActivity.this).setTitle("登录成功，正在下载最新题库...").
                                setView(new ProgressBar(LoginActivity.this)).setCancelable(false).show();
                        sp = getSharedPreferences("test",
                                Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("number", et_phone.getText().toString());
                        editor.putString("password", et_mima.getText().toString());
                        editor.commit();
                        final String url = sp.getString("FormalDBURL", "");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                downFile(url);
                            }
                        }).start();
                    } else {
                        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "网络连接有问题", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", et_phone.getText().toString().trim());
                map.put("password", et_mima.getText().toString().trim());
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    //通过handler更改主线程
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 2://下载完成
                        Toast.makeText(LoginActivity.this, "下载完成", Toast.LENGTH_LONG).show();
                        database = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.normalFilename, ConfigUtil.mi_ma, null);
                        db = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.cuoTiFilename, ConfigUtil.mi_ma, null);
                        UseXiTiSQLite.creatTable(database, db);
                        Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                        break;
                    default:
                        break;
                }
            }
        }

    };

    private void downFile(String urlString) {
                     /*
               * 连接到服务器
               */

        try {
            URL url = new URL(urlString);
            connection = url.openConnection();
            if (connection.getReadTimeout() == 5) {
                Log.i("", "网络连接超时");
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
            outputStream = openFileOutput("ZYformal.db", Context.MODE_PRIVATE);
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
