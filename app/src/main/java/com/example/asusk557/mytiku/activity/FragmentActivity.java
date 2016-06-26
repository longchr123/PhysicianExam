package com.example.asusk557.mytiku.activity;

//在这里得到不同的数据库，习题一个数据库，错题一个数据库

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.ShareDialog;
import com.example.asusk557.mytiku.adapter.FragmentAdapter;
import com.example.asusk557.mytiku.fragment.TestFragment;
import com.example.asusk557.mytiku.javaBean.TestItem;
import com.example.asusk557.mytiku.util.ConfigUtil;
import com.example.asusk557.mytiku.util.DownLoadSQLite;
import com.example.asusk557.mytiku.util.UseCuoTiSQLite;
import com.example.asusk557.mytiku.util.UseXiTiSQLite;
import com.umeng.analytics.MobclickAgent;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class FragmentActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, PlatformActionListener, TestFragment.FragmentCallBack {
    private ViewPager vp;
    private TextView tv_percent, tv_last, tv_next, tv_subject, tv_search, tv_share;
    private List<Fragment> fragments = new ArrayList<>();
    private int currentPosition = 1;
    private ImageView iv_back;
    private SharedPreferences sp;
    private String number, password, subject, table, utilPosition;//那个章节
    private int tableName, tikuPosition;//保存到第几题
    private List<TestItem> list;
    private boolean isXiTi, isShow = false, isLogin;//isXiTi用于判断是否为习题进入，isShow用于判断是否显示查看,isLogin判断是否登录
    private PopupWindow popupWindow;
    private SQLiteDatabase database, cuoSql;
    private ShareDialog shareDialog;
    private String cuoTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ShareSDK.initSDK(this);
        initShow();
        getIntentData();
        openDB();
        qurewDB();

        initView();
        initData();
        initListener();
    }

    private void initShow() {
        sp = getSharedPreferences("test", Activity.MODE_PRIVATE);
        number = sp.getString("number", "");
        password = sp.getString("password", "");
        if (number == null || number == "" || password == null || password == "") {
            isLogin = false;
        } else {
            isLogin = true;
        }
    }

    private void getIntentData() {
        isXiTi = getIntent().getBooleanExtra("isXiTi", true);
        subject = getIntent().getStringExtra("subject");
        tableName = getIntent().getIntExtra("tableName", 0);
        if (isXiTi) {
            utilPosition = getIntent().getStringExtra("utilPosition");
        }
    }


    private void openDB() {
        if (!isXiTi) {//错题
            database = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.normalFilename, ConfigUtil.mi_ma, null);
            table = UseXiTiSQLite.getTableEnglishName(database).get(tableName);
        } else {//习题
            if (isLogin) {
                database = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.normalFilename, ConfigUtil.mi_ma, null);
                table = UseXiTiSQLite.getTableEnglishName(database).get(tableName);
            } else {//试用
                database = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.newFilename, ConfigUtil.mi_ma, null);
                table = UseXiTiSQLite.getTableEnglishName(database).get(tableName);
            }
        }
        cuoSql = SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.cuoTiFilename, ConfigUtil.mi_ma, null);
        cuoTableName = UseXiTiSQLite.getTableEnglishName(database).get(tableName);
    }

    private void qurewDB() {
        if (isXiTi) {//习题
            if (isLogin) {
                list = UseXiTiSQLite.GetTableData(database, table);
            } else {//试用
                list = UseXiTiSQLite.GetTableData(database, table);
            }
        } else {//错题
            list = UseCuoTiSQLite.GetTableData(table);
        }
    }

    private void initListener() {
        vp.setOnPageChangeListener(this);
        tv_last.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_share.setOnClickListener(this);
    }

    //使用数据库遍历出来的数据
    private void initData() {
        tikuPosition = sp.getInt(utilPosition, 1);
        if (list.size() == 0) {
            tv_percent.setText("0/" + list.size());
            Toast.makeText(this, "暂无数据,去习题练练手吧", Toast.LENGTH_SHORT).show();
        } else {
            tv_percent.setText("1/" + list.size());
        }
        if (subject.length() > 10) {
            tv_subject.setText(subject.substring(0, 10) + "...");
        } else {
            tv_subject.setText(subject);
        }
        if (isXiTi && isLogin) {
            new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("您上次做到第" + tikuPosition + "题,继续上次答题？").
                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            vp.setCurrentItem(tikuPosition - 1);
                        }
                    }).setNegativeButton("取消", null).show();
        }
        for (int i = 0; i < list.size(); i++) {
            TestFragment fragment = new TestFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("TestItem", list.get(i));
            tv_share.setVisibility(View.VISIBLE);
            bundle.putBoolean("isXiTi", isXiTi);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
    }

    private void initView() {
        tv_percent = (TextView) findViewById(R.id.tv_percent);
        vp = (ViewPager) findViewById(R.id.vp);
        tv_last = (TextView) findViewById(R.id.tv_last);
        tv_next = (TextView) findViewById(R.id.tv_next);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_subject = (TextView) findViewById(R.id.tv_subject);
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_share = (TextView) findViewById(R.id.tv_share);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position + 1;
        tv_percent.setText(currentPosition + "/" + list.size());
        if (currentPosition == 10) {
            if (number == null || number == "" || password == null || password == "") {
                new AlertDialog.Builder(FragmentActivity.this)
                        .setTitle("登录之后更多海量题库等你来战")
                        .setMessage("确定现在登录吗？")
                        .setPositiveButton("现在登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(FragmentActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_last:
                if (currentPosition == 1) {
                    Toast.makeText(this, "已经到第一页了", Toast.LENGTH_SHORT).show();
                } else {
                    currentPosition--;
                    vp.setCurrentItem(currentPosition - 1);
                    tv_percent.setText((currentPosition) + "/" + list.size());
                }
                break;
            case R.id.tv_next:
                if (currentPosition >= list.size()) {
                    Toast.makeText(this, "已经到了最后一页", Toast.LENGTH_SHORT).show();
                } else {
                    currentPosition++;
                    vp.setCurrentItem(currentPosition - 1);
                    tv_percent.setText(currentPosition + "/" + list.size());
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                if (number == null || number == "" || password == null || password == "") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("你还没有登录，现在登录吗？");

                    builder.setTitle("提示");

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(FragmentActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();
                } else {
                    if (!isShow) {
                        showSumSelectWindows();
                        isShow = true;
                    } else {
                        popupWindow.dismiss();
                        isShow = false;
                    }
                }
                break;
            case R.id.tv_share:
                shareDialog = new ShareDialog(FragmentActivity.this);
                shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();

                    }
                });
                shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                        if (item.get("ItemText").equals("新浪微博")) {
                            Platform.ShareParams sp = new Platform.ShareParams();
                            TestItem testItem = list.get(currentPosition - 1);
                            sp.setText("中域题库：" + testItem.get题干() + "( )" + ",答案：" + testItem.get答案() + ",解析：" + testItem.get解析() + "海量题库尽在中域，了解更多，请下载中域题库:http://www.zhongyuedu.com/app");
                            sp.setImageUrl("http://www.zhongyuedu.com/app/images/ic_formal.jpg");
                            Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                            sinaWeibo.SSOSetting(true);
                            sinaWeibo.setPlatformActionListener(FragmentActivity.this); // 设置分享事件回调
                            // 执行分享
                            sinaWeibo.share(sp);
                        } else if (item.get("ItemText").equals("QQ")) {
                            Platform.ShareParams sp = new Platform.ShareParams();
                            sp.setTitle("中域题库");
                            sp.setTitleUrl("http://www.zhongyuedu.com/app"); // 标题的超链接
                            TestItem testItem = list.get(currentPosition - 1);
                            sp.setText("中域题库：" + testItem.get题干() + "( )" + ",答案：" + testItem.get答案() + ",解析：" + testItem.get解析() + "海量题库尽在中域，了解更多，请下载中域题库:http://www.zhongyuedu.com/app");
                            sp.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_formal));

                            Platform qq = ShareSDK.getPlatform(QQ.NAME);
                            qq.setPlatformActionListener(FragmentActivity.this); // 设置分享事件回调
                            // 执行分享
                            qq.share(sp);
                        } else if (item.get("ItemText").equals("微信好友")) {
                            //2、设置分享内容
                            Platform.ShareParams sp = new Platform.ShareParams();
                            sp.setShareType(Platform.SHARE_TEXT);//非常重要：一定要设置分享属性
                            sp.setTitle("中域题库");  //分享标题
                            sp.setTitleUrl("http://www.zhongyuedu.com/app"); // 标题的超链接
                            TestItem testItem = list.get(currentPosition - 1);
                            sp.setText("中域题库：" + testItem.get题干() + "( )" + ",答案：" + testItem.get答案() + ",解析：" + testItem.get解析() + "海量题库尽在中域，了解更多，请下载中域题库:http://www.zhongyuedu.com/app");   //分享文本
//                            sp.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_formal));
                            sp.setUrl("http://www.zhongyuedu.com/app");   //网友点进链接后，可以看到分享的详情

                            //3、非常重要：获取平台对象
                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                            wechat.setPlatformActionListener(FragmentActivity.this); // 设置分享事件回调
                            // 执行分享
                            wechat.share(sp);

                        } else if (item.get("ItemText").equals("QQ空间")) {
                            //2、设置分享内容
                            Platform.ShareParams sp = new Platform.ShareParams();
                            sp.setTitle("中域题库");  //分享标题
                            sp.setTitleUrl("http://www.zhongyuedu.com/app"); // 标题的超链接
                            TestItem testItem = list.get(currentPosition - 1);
                            sp.setText("中域题库：" + testItem.get题干() + "( )" + ",答案：" + testItem.get答案() + ",解析：" + testItem.get解析() + "海量题库尽在中域，了解更多，请下载中域题库:http://www.zhongyuedu.com/app");   //分享文本
                            sp.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_formal));
                            sp.setUrl("http://www.zhongyuedu.com/app");   //网友点进链接后，可以看到分享的详情
                            //3、非常重要：获取平台对象
                            Platform qZone = ShareSDK.getPlatform(QZone.NAME);
                            qZone.setPlatformActionListener(FragmentActivity.this); // 设置分享事件回调
                            // 执行分享
                            qZone.share(sp);

                        } else if (item.get("ItemText").equals("微信朋友圈")) {
                            Platform.ShareParams sp = new Platform.ShareParams();
                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                            sp.setTitle("中域题库");  //分享标题
                            TestItem testItem = list.get(currentPosition - 1);
                            sp.setText("中域题库：" + testItem.get题干() + "( )" + ",答案：" + testItem.get答案() + ",解析：" + testItem.get解析() + "海量题库尽在中域，了解更多，请下载中域题库:http://www.zhongyuedu.com/app");   //分享文本
                            sp.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_formal));
                            sp.setUrl("http://www.zhongyuedu.com/app");   //网友点进链接后，可以看到分享的详情

                            //3、非常重要：获取平台对象
                            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                            wechatMoments.setPlatformActionListener(FragmentActivity.this); // 设置分享事件回调
                            // 执行分享
                            wechatMoments.share(sp);
                        } else {
                            Toast.makeText(FragmentActivity.this, "您点中了" + item.get("ItemText"), Toast.LENGTH_LONG).show();
                        }
                        shareDialog.dismiss();
                    }
                });
                break;
        }
    }

    //搜索指定题数
    public void showSumSelectWindows() {
        View view = LayoutInflater.from(this).inflate(R.layout.popwindows_sum, null, false);
        popupWindow = new PopupWindow(view);
        GridView gv = (GridView) view.findViewById(R.id.gv);
        gv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public String getItem(int position) {
                return (position + 1) + "";
            }

            @Override
            public long getItemId(int position) {
                return position;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(FragmentActivity.this).inflate(R.layout.item_popwindow, null, false);
                }
                TextView textView = (TextView) convertView.findViewById(R.id.tv);
                textView.setText(getItem(position));

                return convertView;
            }
        });
        popupWindow.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        // popupWindow.setOutsideTouchable(false);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(findViewById(R.id.tv_subject));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                vp.setCurrentItem(position);
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow == null) {
                finish();
            } else if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else if (!popupWindow.isShowing()) {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isXiTi && isLogin) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(utilPosition, currentPosition);
            editor.commit();
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 3;
        msg.obj = arg2.getMessage();
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(2);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "分享取消", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "分享失败" + msg.obj, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
        if (cuoSql != null) {
            cuoSql.close();
        }
    }

    @Override
    public void addToSql(int testId) {
        if (!panDuan(testId)) {
            ContentValues values = new ContentValues();
            values.put("题干", list.get(currentPosition - 1).get题干());
            values.put("A", list.get(currentPosition - 1).getA());
            values.put("B", list.get(currentPosition - 1).getB());
            values.put("C", list.get(currentPosition - 1).getC());
            values.put("D", list.get(currentPosition - 1).getD());
            values.put("E", list.get(currentPosition - 1).getE());
            values.put("答案", list.get(currentPosition - 1).get答案());
            values.put("解析", list.get(currentPosition - 1).get解析());
            values.put("testId", testId);
            cuoSql.insert(cuoTableName, "id", values);
        }
        Toast.makeText(this, "回答错误，已经加入错题复习", Toast.LENGTH_LONG).show();
    }

    @Override
    public void delFromSql(int id) {
        cuoSql.delete(cuoTableName, "id=" + id, null);
        Toast.makeText(this, "删除成功，返回查看", Toast.LENGTH_SHORT).show();
    }

    private boolean panDuan(int id) {//true不要加，false要加
        Cursor cursor = cuoSql.query(cuoTableName, new String[]{"id", "题干", "A", "B", "C", "D", "E", "答案", "解析", "testId"}, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int testId = cursor.getInt(cursor.getColumnIndex("testId"));
            if (id == testId) {
                return true;
            }
        }
        cursor.close();
        return false;
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

