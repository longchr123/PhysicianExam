package com.example.asusk557.mytiku.activity;
//引导页面

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.asusk557.mytiku.R;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private String choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        JPushInterface.init(this);
        JPushInterface.setAlias(this,"15764241281",null);
        sp = getSharedPreferences("test", Activity.MODE_PRIVATE);
        choose = sp.getString("choose", "");
        final Intent it = new Intent(this, GuideActivity.class);
        final Intent intent = new Intent(this, Main2Activity.class);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (choose == null || choose == "") {
                    startActivity(it); //执行
                } else {
                    startActivity(intent);
                }
                finish();
            }
        };
        timer.schedule(task, 1000 * 1); //2秒后
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }
}
