package com.example.asusk557.mytiku.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asusk557.mytiku.R;
import com.umeng.analytics.MobclickAgent;

public class AdviceActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_tiJiao;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        initView();
        initListener();

    }

    private void initListener() {
        tv_tiJiao.setOnClickListener(this);
    }

    private void initView() {
        tv_tiJiao = (TextView) findViewById(R.id.tv_tiJiao);
        et_content = (EditText) findViewById(R.id.et_content);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(AdviceActivity.this, "提交成功,谢谢！", Toast.LENGTH_SHORT).show();
        finish();
//        RequestQueue mQueue = Volley.newRequestQueue(this);
//        String url="http://";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,  new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(AdviceActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(AdviceActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("content", et_content.getText().toString());
//                return map;
//            }
//        };
//        mQueue.add(stringRequest);
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
