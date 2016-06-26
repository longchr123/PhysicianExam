package com.example.asusk557.mytiku.receiver;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.asusk557.mytiku.MainActivity;
import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.activity.Main2Activity;

import java.io.IOException;

/**
 * Created by Administrator on 2016/4/25.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager nm;
    private MediaPlayer mMediaPlayer;
    private Vibrator vibrate;

    public void onReceive(Context context, Intent intent) {
        if ("android.alarm.tiku.action".equals(intent.getAction())) {
            //第1步中设置的闹铃时间到，这里可以弹出闹铃提示并播放响铃
            //可以继续设置下一次闹铃时间;
            NotificationCompat.Builder mBuilder2 = new NotificationCompat.Builder(context);
            mBuilder2.setTicker("主人，该做题了，你准备好了吗？");
            mBuilder2.setSmallIcon(R.mipmap.ic_formal);
            mBuilder2.setContentTitle("执业医师题库");
            mBuilder2.setContentText("开始作答吧，祝你好运！");

            //设置点击一次后消失（如果没有点击事件，则该方法无效。）
            mBuilder2.setAutoCancel(true);

            //点击通知之后需要跳转的页面
            Intent resultIntent = new Intent(context, Main2Activity.class);

            //使用TaskStackBuilder为“通知页面”设置返回关系
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            //为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder2.setContentIntent(pIntent);
            // mId allows you to update the notification later on.
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1, mBuilder2.build());
            // 等待3秒，震动3秒，-1表示不循环，1表示循环播放
            vibrate = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrate.vibrate(new long[]{1000, 1000}, -1);

            sound(context);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
            //解锁
            kl.disableKeyguard();
            //获取电源管理器对象
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            //点亮屏幕
            wl.acquire();
            //释放
            wl.release();
        }
    }

    // 使用来电铃声的铃声路径
    private void sound(Context context) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
