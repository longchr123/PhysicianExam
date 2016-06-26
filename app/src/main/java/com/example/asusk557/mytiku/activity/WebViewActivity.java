package com.example.asusk557.mytiku.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.ShareDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class WebViewActivity extends AppCompatActivity implements PlatformActionListener {
    private WebView webView;
    private TextView tv_share, tv_top;
    private ShareDialog shareDialog;
    private String url, title, top;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ShareSDK.initSDK(this);
        getIntentData();
        initView();
        initData();
        initListener();
        init();
    }

    private void initData() {
        tv_top.setText(top);
        if (!top.equals("新闻资讯")) {
            tv_share.setVisibility(View.GONE);
        }
    }

    private void getIntentData() {
        top = getIntent().getStringExtra("top");
        url = getIntent().getStringExtra("webUrl");
        title = getIntent().getStringExtra("title");
    }

    private void initListener() {
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog = new ShareDialog(WebViewActivity.this);
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
                            sp.setText("关注医考，关注新闻动态，" + title + ",新闻链接：" + url + ",医考的最新动态尽在中域题库，了解更多，请下载中域题库:http://www.zhongyuedu.com/app");
                            sp.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_formal));

                            Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                            sinaWeibo.SSOSetting(true);
                            sinaWeibo.setPlatformActionListener(WebViewActivity.this); // 设置分享事件回调
                            // 执行分享
                            sinaWeibo.share(sp);
                        } else if (item.get("ItemText").equals("QQ")) {
                            Platform.ShareParams sp = new Platform.ShareParams();
                            sp.setTitle("中域题库");
                            sp.setTitleUrl("http://www.zhongyuedu.com/app"); // 标题的超链接
                            sp.setText("关注医考，关注新闻动态，" + title + ",新闻链接：" + url + ",医考的最新动态尽在中域题库");
                            sp.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_formal));

                            Platform qq = ShareSDK.getPlatform(QQ.NAME);
                            qq.setPlatformActionListener(WebViewActivity.this); // 设置分享事件回调
                            // 执行分享
                            qq.share(sp);
                        } else if (item.get("ItemText").equals("微信好友")) {
                            //2、设置分享内容
                            Platform.ShareParams sp = new Platform.ShareParams();
                            sp.setShareType(Platform.SHARE_TEXT);//非常重要：一定要设置分享属性
                            sp.setTitle("中域题库");  //分享标题
                            sp.setTitleUrl("http://www.zhongyuedu.com/app"); // 标题的超链接
                            sp.setText(title + ":" + url);   //分享文本
//                            sp.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_formal));
                            sp.setUrl("http://www.zhongyuedu.com/app");   //网友点进链接后，可以看到分享的详情

                            //3、非常重要：获取平台对象
                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                            wechat.setPlatformActionListener(WebViewActivity.this); // 设置分享事件回调
                            // 执行分享
                            wechat.share(sp);

                        } else if (item.get("ItemText").equals("QQ空间")) {
                            //2、设置分享内容
                            Platform.ShareParams sp = new Platform.ShareParams();
                            sp.setTitle("中域题库");  //分享标题
                            sp.setTitleUrl("http://www.zhongyuedu.com/app"); // 标题的超链接
                            sp.setText(title + ":" + url);   //分享文本
                            sp.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_formal));
                            sp.setUrl("http://www.zhongyuedu.com/app");   //网友点进链接后，可以看到分享的详情
                            //3、非常重要：获取平台对象
                            Platform qZone = ShareSDK.getPlatform(QZone.NAME);
                            qZone.setPlatformActionListener(WebViewActivity.this); // 设置分享事件回调
                            // 执行分享
                            qZone.share(sp);

                        } else if (item.get("ItemText").equals("微信朋友圈")) {
                            Platform.ShareParams sp = new Platform.ShareParams();
                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                            sp.setTitle("中域题库");  //分享标题
                            sp.setText("关注医考，关注新闻动态，" + title + ",新闻链接：" + url + ",医考的最新动态尽在中域题库，了解更多，请下载中域题库:http://www.zhongyuedu.com/app");   //分享文本
                            sp.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_formal));
                            sp.setUrl("http://www.zhongyuedu.com/app");   //网友点进链接后，可以看到分享的详情

                            //3、非常重要：获取平台对象
                            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                            wechatMoments.setPlatformActionListener(WebViewActivity.this); // 设置分享事件回调
                            // 执行分享
                            wechatMoments.share(sp);
                        } else {
                            Toast.makeText(WebViewActivity.this, "您点中了" + item.get("ItemText"), Toast.LENGTH_LONG).show();
                        }
                        shareDialog.dismiss();

                    }
                });
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        tv_top = (TextView) findViewById(R.id.tv_top);
        tv_share = (TextView) findViewById(R.id.tv_share);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    private void init() {
        webView = (WebView) findViewById(R.id.wv);
        //WebView加载web资源
        webView.loadUrl(url);
        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //设置支持缩放
        settings.setBuiltInZoomControls(true);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {//回调的地方是子线程，进行UI操作要用handle处理
        handler.sendEmptyMessage(2);

    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        handler.sendEmptyMessage(1);


    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 3;
        msg.obj = arg2.getMessage();
        handler.sendMessage(msg);
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
