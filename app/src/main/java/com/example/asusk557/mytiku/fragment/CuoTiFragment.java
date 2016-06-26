package com.example.asusk557.mytiku.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.activity.FragmentActivity;
import com.example.asusk557.mytiku.activity.LoginActivity;
import com.example.asusk557.mytiku.activity.Main2Activity;
import com.example.asusk557.mytiku.adapter.MyListViewAdapter;
import com.example.asusk557.mytiku.util.ConfigUtil;
import com.example.asusk557.mytiku.util.DownLoadSQLite;
import com.example.asusk557.mytiku.util.UseXiTiSQLite;
import com.example.asusk557.mytiku.view.MyListView;
import com.umeng.analytics.MobclickAgent;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.List;

/**
 * Created by ASUSK557 on 2016/2/16.
 */
public class CuoTiFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Main2Activity main2Activity;
    private View view;
    private LinearLayout lin_weiDengLu, lin_yiDengLu;
    private SharedPreferences sp;
    private String number, password;
    private Button bt_login;
    private MyListView mlv;
    private MyListViewAdapter adapter;
    private List<String> list;//错题集主页的列表
    private boolean isLogin;
    private SQLiteDatabase database;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main2Activity = (Main2Activity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.from(main2Activity).inflate(R.layout.fragment_cuoti, null);
        dialog=new AlertDialog.Builder(main2Activity).setTitle("加载中...").
                setView(new ProgressBar(main2Activity)).setCancelable(false).create();
        initShow();
        initView();
        initData();
        initListener();
        return view;
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
        mlv.setOnItemClickListener(this);
    }

    private void initShow() {
        sp = main2Activity.getSharedPreferences("test", Activity.MODE_PRIVATE);
        number = sp.getString("number", "");
        password = sp.getString("password", "");
        if (number == null || number == "" || password == null || password == "") {
            isLogin = false;
        } else {
            isLogin = true;
        }
    }

    private void initData() {
        if (isLogin) {
            lin_yiDengLu.setVisibility(view.VISIBLE);
            lin_weiDengLu.setVisibility(view.INVISIBLE);
            if (new File(DownLoadSQLite.normalFilename).exists()) {
                //遍历数据库文件获取数据
                database=SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.normalFilename, ConfigUtil.mi_ma,null);
                list = UseXiTiSQLite.getTableChineseName(database);
                adapter = new MyListViewAdapter(main2Activity, list,false);
                mlv.setAdapter(adapter);
            } else {
                Toast.makeText(main2Activity, "暂无数据,请在习题中更新数据", Toast.LENGTH_SHORT).show();
            }
        } else {
            lin_yiDengLu.setVisibility(view.INVISIBLE);
            lin_weiDengLu.setVisibility(view.VISIBLE);
        }
    }

    private void initView() {
        lin_weiDengLu = (LinearLayout) view.findViewById(R.id.lin_weiDengLu);
        lin_yiDengLu = (LinearLayout) view.findViewById(R.id.lin_yiDengLu);
        bt_login = (Button) view.findViewById(R.id.bt_login);
        mlv = (MyListView) view.findViewById(R.id.mlv);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dialog.show();
        Intent intent = new Intent(main2Activity, FragmentActivity.class);
        intent.putExtra("isXiTi", false);
        intent.putExtra("subject", list.get(position));
        intent.putExtra("tableName", position);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        dialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(database!=null){
            database.close();
        }
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
