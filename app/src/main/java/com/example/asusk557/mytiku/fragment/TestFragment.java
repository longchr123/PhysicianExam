package com.example.asusk557.mytiku.fragment;


import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.activity.FragmentActivity;
import com.example.asusk557.mytiku.adapter.MyTestAdapter;
import com.example.asusk557.mytiku.javaBean.TestItem;
import com.example.asusk557.mytiku.util.ConfigUtil;
import com.example.asusk557.mytiku.util.DownLoadSQLite;
import com.umeng.analytics.MobclickAgent;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/30.
 */
public class TestFragment extends Fragment {
    private FragmentActivity fragmentActivity;
    private TextView tv_content, tv_top, tv_answer, tv_jiexi, tv_bottom, tv_jieXi;
    private Button tv_showOrDel;
    private ListView lv;
    private View view;
    private List<String> list;
    private MyTestAdapter adapter;
    private TestItem testItem;
    private SharedPreferences sp;
    private boolean flag = true;//flag用来只能选择一次
    private boolean isXiti;
    private boolean isLogin;
    private String number, password;
    private FragmentCallBack callBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity = (FragmentActivity) getActivity();
        Bundle bundle = getArguments();
        testItem = (TestItem) bundle.getSerializable("TestItem");
        isXiti = bundle.getBoolean("isXiTi");
        callBack=fragmentActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, null);
        isShow();
        initView();
        initData();
        initListener();
        return view;
    }

    private void isShow() {
        sp = fragmentActivity.getSharedPreferences("test", Activity.MODE_PRIVATE);
        number = sp.getString("number", "");
        password = sp.getString("password", "");
        if (number == null || number == "" || password == null || password == "") {
            isLogin = false;
        } else {
            isLogin = true;
        }
    }

    private void initListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (flag) {
                    tv_showOrDel.setVisibility(View.GONE);
                    tv_top.setVisibility(View.VISIBLE);
                    tv_jiexi.setVisibility(View.VISIBLE);
                    tv_answer.setVisibility(View.VISIBLE);
                    tv_bottom.setVisibility(View.VISIBLE);
                    tv_jieXi.setVisibility(View.VISIBLE);
                    adapter.changeTextColor(position);
                    if (list.get(position).substring(0, 1).equals(testItem.get答案())) {
                        Toast.makeText(fragmentActivity, "恭喜回答正确咯", Toast.LENGTH_SHORT).show();
                    } else {
                        if (isXiti) {
                            //调用方法插入数据
                            if (isLogin) {
                                callBack.addToSql(testItem.getId());//插入数据
                            } else {
                                Toast.makeText(fragmentActivity, "回答错误了，请继续加油！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(fragmentActivity, "回答错误了，请继续加油！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    flag = false;
                }
            }
        });
        tv_showOrDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isXiti) {
                    tv_showOrDel.setVisibility(View.GONE);
                    tv_top.setVisibility(View.VISIBLE);
                    tv_jiexi.setVisibility(View.VISIBLE);
                    tv_answer.setVisibility(View.VISIBLE);
                    tv_bottom.setVisibility(View.VISIBLE);
                    tv_jieXi.setVisibility(View.VISIBLE);
                } else {
                    callBack.delFromSql(testItem.getId());//删除数据
                }
            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        list.add("A." + testItem.getA());
        list.add("B." + testItem.getB());
        list.add("C." + testItem.getC());
        list.add("D." + testItem.getD());
        if (!testItem.getE().equals("暂无") && !testItem.getE().equals("")) {
            list.add("E." + testItem.getE());
        }
        adapter = new MyTestAdapter(fragmentActivity, list);
        lv.setAdapter(adapter);
        tv_content.setText(testItem.get题干() + "(  )");
        tv_jieXi.setText(testItem.get解析());
        tv_answer.setText("正确答案：" + testItem.get答案());
        sp = fragmentActivity.getSharedPreferences("test", Activity.MODE_PRIVATE);
        if (isXiti) {
            tv_showOrDel.setText("显示答案");
        } else {
            tv_showOrDel.setText("删除");
        }
    }

    private void initView() {
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_top = (TextView) view.findViewById(R.id.tv_top);
        tv_answer = (TextView) view.findViewById(R.id.tv_answer);
        tv_jiexi = (TextView) view.findViewById(R.id.tv_jiexi);
        tv_bottom = (TextView) view.findViewById(R.id.tv_bottom);
        tv_jieXi = (TextView) view.findViewById(R.id.tv_jieXi);
        tv_showOrDel = (Button) view.findViewById(R.id.tv_showOrDel);
    }

    public interface FragmentCallBack {
        void addToSql(int testId);

        void delFromSql(int id);
    }

    @Override
    public void onResume() {
        super.onResume();
        flag = true;
        MobclickAgent.onPageStart("FragmentActivity"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FragmentActivity");
    }
}
