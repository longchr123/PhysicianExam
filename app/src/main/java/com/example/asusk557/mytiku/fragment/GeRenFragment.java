package com.example.asusk557.mytiku.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.activity.AdviceActivity;
import com.example.asusk557.mytiku.activity.AppActivity;
import com.example.asusk557.mytiku.activity.LoginActivity;
import com.example.asusk557.mytiku.activity.Main2Activity;
import com.example.asusk557.mytiku.activity.WebViewActivity;
import com.example.asusk557.mytiku.util.UpdateChecker;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ASUSK557 on 2016/2/16.
 */
public class GeRenFragment extends Fragment {
    private Main2Activity main2Activity;
    private View view;
    private LinearLayout lin_weiDengLu, lin_yiDengLu;
    private SharedPreferences sp;
    private String number, password, exam;
    private Button bt_login;
    private TextView tv_1, tv_2, tv_3, tv_4, tv_phone, tv_exam, tv, tv_5;
    private boolean isLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main2Activity = (Main2Activity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.from(main2Activity).inflate(R.layout.fragment_geren, null);

        initShow();
        initView();
        initData();
        initListener();
        return view;
    }

    private void initData() {
        if (isLogin) {
            lin_yiDengLu.setVisibility(view.VISIBLE);
            lin_weiDengLu.setVisibility(view.INVISIBLE);
            tv_phone.setText(number.substring(0, 3) + "****" + number.substring(7, 11));
            tv_exam.setText(exam);
        } else {
            lin_yiDengLu.setVisibility(view.INVISIBLE);
            lin_weiDengLu.setVisibility(view.VISIBLE);
        }
    }

    private void initListener() {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main2Activity, LoginActivity.class);
                startActivity(intent);
                main2Activity.finish();
            }
        });
        //关于我们
        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main2Activity, WebViewActivity.class);
                intent.putExtra("top", "关于我们");
                intent.putExtra("title", "");
                intent.putExtra("webUrl", "http://www.zhongyuedu.com/api/aboutUS.htm");
                startActivity(intent);
            }
        });
        //使用说明
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main2Activity, WebViewActivity.class);
                intent.putExtra("top", "使用说明");
                intent.putExtra("title", "");
                intent.putExtra("webUrl", "http://www.zhongyuedu.com/api/tk_ios_usage.htm");
                startActivity(intent);
            }
        });
        //检查新版本
        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateChecker updateChecker = new UpdateChecker(main2Activity);
                updateChecker.setCheckUrl("http://www.zhongyuedu.com/api/tk_version_android.php");
                updateChecker.checkForUpdates();
            }
        });
        //意见建议
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main2Activity, AdviceActivity.class);
                startActivity(intent);
            }
        });
        //联系客服
        tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4008-355-366"));
                main2Activity.startActivity(intent);
            }
        });
        //应用推荐
        tv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(main2Activity, AppActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initShow() {
        sp = main2Activity.getSharedPreferences("test", Activity.MODE_PRIVATE);
        number = sp.getString("number", "");
        password = sp.getString("password", "");
        exam = sp.getString("choose", "");
        if (number == null || number == "" || password == null || password == "") {
            isLogin = false;
        } else {
            isLogin = true;
        }
    }

    private void initView() {
        lin_weiDengLu = (LinearLayout) view.findViewById(R.id.lin_weiDengLu);
        lin_yiDengLu = (LinearLayout) view.findViewById(R.id.lin_yiDengLu);
        bt_login = (Button) view.findViewById(R.id.bt_login);
        tv_1 = (TextView) view.findViewById(R.id.tv_1);
        tv_2 = (TextView) view.findViewById(R.id.tv_2);
        tv_3 = (TextView) view.findViewById(R.id.tv_3);
        tv_4 = (TextView) view.findViewById(R.id.tv_4);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_exam = (TextView) view.findViewById(R.id.tv_exam);
        tv = (TextView) view.findViewById(R.id.tv);
        tv_5 = (TextView) view.findViewById(R.id.tv_5);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Main2Activity"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Main2Activity");
    }

}
