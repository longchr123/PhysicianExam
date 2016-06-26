package com.example.asusk557.mytiku.activity;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.fragment.CuoTiFragment;
import com.example.asusk557.mytiku.fragment.GeRenFragment;
import com.example.asusk557.mytiku.fragment.NewsFragment;
import com.example.asusk557.mytiku.fragment.XiTiFragment;
import com.umeng.analytics.MobclickAgent;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class Main2Activity extends AppCompatActivity implements OnClickListener {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment mCurrentFragment;
    private RadioButton rb_xiti, rb_cuoti, rb_geren, rb_news;
    private XiTiFragment xitiFragment;
    private CuoTiFragment cuotiFragment;
    private GeRenFragment gerenFragment;
    private NewsFragment newsFragment;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.administrator.day0303.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SQLiteDatabase.loadLibs(this);
        initView();
        initData();
        initListener();
        //推送功能
        registerMessageReceiver();
        setAlarm(20, 0);
    }

    private void setAlarm(int hour, int minute) {
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        // 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        long firstTime = systemTime + time;
        setAlarmTime(this, firstTime);
    }

    private void setAlarmTime(Context context, long firstTime) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("android.alarm.tiku.action");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int interval = 60 * 1000 * 60 * 24;//闹铃时间间隔
        am.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, interval, sender);
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
//				if (!ExampleUtil.isEmpty(extras)) {
//					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
//				}
//                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void initListener() {
        rb_xiti.setOnClickListener(this);
        rb_cuoti.setOnClickListener(this);
        rb_geren.setOnClickListener(this);
        rb_news.setOnClickListener(this);
    }

    private void initData() {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        xitiFragment = new XiTiFragment();
        transaction.add(R.id.lin, xitiFragment).commit();
        changeTextColor(rb_xiti);
        mCurrentFragment = xitiFragment;
        cuotiFragment = new CuoTiFragment();
        gerenFragment = new GeRenFragment();
        newsFragment = new NewsFragment();
    }

    private void initView() {
        rb_xiti = (RadioButton) findViewById(R.id.rb_xiti);
        rb_cuoti = (RadioButton) findViewById(R.id.rb_cuoti);
        rb_geren = (RadioButton) findViewById(R.id.rb_geren);
        rb_news = (RadioButton) findViewById(R.id.rb_news);
    }

    public void changeFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        transaction = manager.beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.lin, fragment);
        }
        transaction.commit();
        mCurrentFragment = fragment;

    }

    private void changeTextColor(RadioButton rb) {
        rb_xiti.setTextColor(Color.WHITE);
        rb_cuoti.setTextColor(Color.WHITE);
        rb_geren.setTextColor(Color.WHITE);
        rb_news.setTextColor(Color.WHITE);
        rb.setTextColor(Color.YELLOW);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_xiti:
                changeFragment(xitiFragment);
                changeTextColor(rb_xiti);
                break;
            case R.id.rb_cuoti:
                changeFragment(cuotiFragment);
                changeTextColor(rb_cuoti);
                break;
            case R.id.rb_geren:
                changeFragment(gerenFragment);
                changeTextColor(rb_geren);
                break;
            case R.id.rb_news:
                changeFragment(newsFragment);
                changeTextColor(rb_news);
                break;
        }
    }

    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
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
