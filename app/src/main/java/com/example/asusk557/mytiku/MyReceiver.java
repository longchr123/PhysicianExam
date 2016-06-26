package com.example.asusk557.mytiku;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/3/7.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            //打开自定义的Activity
            Intent i = new Intent(context,TestActivity.class);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }
//        Log.e("MyReceiver","------"+intent.getAction());
//        Log.e("MyReceiver","------"+JPushInterface.ACTION_NOTIFICATION_OPENED);
//        Log.e("MyReceiver","------"+intent.getAction());
//        Log.e("MyReceiver","------"+intent.getAction());
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
            StringBuilder sb = new StringBuilder();
//            Intent i = new Intent(context,TestActivity.class);
//            i.putExtras(bundle);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);
            for (String key : bundle.keySet()) {
               sb.append(bundle.getString(key));
            }
            Log.e("MyReceiver",sb.toString());
        }
    }
}
