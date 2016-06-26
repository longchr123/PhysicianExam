package com.example.asusk557.mytiku.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText phone, et_mima, et_mima2, cord;
    private TextView now, getCord, tv_content, tv_login;
    private LinearLayout lin_back;
    private Button saveCord;
    private String iPhone, iCord;
    private int time = 60;
    private boolean flag = true;
    private StringRequest stringRequest;
    private RequestQueue mQueue;
    private SharedPreferences sp;
    private SQLiteDatabase db, database;
    private URLConnection connection;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Dialog dialog;
    private String tikuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initView();
        initData();
        initListener();
        SMSSDK.initSDK(this, "fc8d7f1ddfc8", "67bda3ac26f77a24b87a92fef9a2ab71");
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }

    private void initData() {
        sp = getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        tikuId = sp.getString("id", "");
    }

    private void initListener() {
        getCord.setOnClickListener(this);
        saveCord.setOnClickListener(this);
        lin_back.setOnClickListener(this);
        tv_content.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        cord = (EditText) findViewById(R.id.cord);
        now = (TextView) findViewById(R.id.now);
        getCord = (TextView) findViewById(R.id.getcord);
        saveCord = (Button) findViewById(R.id.savecord);
        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        et_mima = (EditText) findViewById(R.id.et_mima);
        et_mima2 = (EditText) findViewById(R.id.et_mima2);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_login = (TextView) findViewById(R.id.tv_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getcord:
                if (!TextUtils.isEmpty(phone.getText().toString().trim())) {
                    if (phone.getText().toString().trim().length() == 11) {
                        iPhone = phone.getText().toString().trim();
                        SMSSDK.getVerificationCode("86", iPhone);
                        cord.requestFocus();
                        getCord.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(this, "请输入完整电话号码", Toast.LENGTH_LONG).show();
                        phone.requestFocus();
                    }
                } else {
                    Toast.makeText(this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
                    phone.requestFocus();
                }
                break;

            case R.id.savecord:
                if (et_mima.getText().toString().length() < 6) {
                    Toast.makeText(this, "请输入至少六位的密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!et_mima.getText().toString().equals(et_mima2.getText().toString())) {
                    Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(cord.getText().toString().trim())) {
                    //验证验证码
                    if (cord.getText().toString().trim().length() == 4) {
                        iCord = cord.getText().toString().trim();
                        SMSSDK.submitVerificationCode("86", iPhone, iCord);
                        flag = false;
                        //通过post请求把号码和密码传到服务器进行注册
                    } else {
                        Toast.makeText(this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                        cord.requestFocus();
                    }
                } else {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show();
                    cord.requestFocus();
                }
                break;

            case R.id.lin_back:
                finish();
                break;
            case R.id.tv_content:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4008-355-366"));
                startActivity(intent);
                break;
            case R.id.tv_login:
                Intent intent2 = new Intent(this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    //验证码送成功后提示文字
    private void reminderText() {
        now.setVisibility(View.VISIBLE);
        handlerText.sendEmptyMessageDelayed(1, 1000);
    }

    Handler handlerText = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (time > 0) {
                    now.setText("验证码已发送" + time + "秒");
                    time--;
                    handlerText.sendEmptyMessageDelayed(1, 1000);
                } else {
                    now.setText("提示信息");
                    time = 60;
                    now.setVisibility(View.GONE);
                    getCord.setVisibility(View.VISIBLE);
                }
            } else {
                cord.setText("");
                now.setText("提示信息");
                time = 60;
                now.setVisibility(View.GONE);
                getCord.setVisibility(View.VISIBLE);
            }
        }

        ;
    };

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                    handlerText.sendEmptyMessage(2);
                    create();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//服务器验证码发送成功
                    reminderText();
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (flag) {
                    getCord.setVisibility(View.VISIBLE);
                    Toast.makeText(CreateActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    phone.requestFocus();
                } else {
                    ((Throwable) data).printStackTrace();
//                    int resId = getStringRes(MainActivity.this, "smssdk_network_error");
                    Toast.makeText(CreateActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    cord.selectAll();
//                    if (resId > 0) {
//                        Toast.makeText(MainActivity.this, resId, Toast.LENGTH_SHORT).show();
//                    }
                }

            }

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }


    //注册逻辑
    public void create() {
        mQueue = Volley.newRequestQueue(this);
        String httpUrl = "http://www.zhongyuedu.com/api/register.php";//公司服务器
        stringRequest = new StringRequest(Request.Method.POST, httpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //服务器返回的内容
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String resultCode = jsonObject.getString("resultCode");
                    String result = jsonObject.getString("result");
                    if (resultCode.equals("0")) {
                        MobclickAgent.onProfileSignIn("userID");
                        Toast.makeText(CreateActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        dialog = new AlertDialog.Builder(CreateActivity.this).setTitle("正在下载最新题库...").
                                setView(new ProgressBar(CreateActivity.this)).setCancelable(false).show();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("number", phone.getText().toString());
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
                        Toast.makeText(CreateActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreateActivity.this, "网络连接有问题", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", phone.getText().toString().trim());
                map.put("password", et_mima.getText().toString().trim());
                map.put("tikuid", tikuId);
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    //通过handler更改主线程
    private Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 2://下载完成
                        Toast.makeText(CreateActivity.this, "下载完成", Toast.LENGTH_LONG).show();
                        database = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.normalFilename, ConfigUtil.mi_ma, null);
                        db = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.cuoTiFilename, ConfigUtil.mi_ma, null);
                        UseXiTiSQLite.creatTable(database, db);
                        Intent intent = new Intent(CreateActivity.this, Main2Activity.class);
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
            handler2.sendMessage(message2);
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
