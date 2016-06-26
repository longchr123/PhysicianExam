package com.example.asusk557.mytiku.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asusk557.mytiku.MainActivity;
import com.example.asusk557.mytiku.R;
import com.example.asusk557.mytiku.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private TextView tv;
    private MyViewPagerAdapter adapter;
    private List<ImageView> imageViews;
    private int[] imageViewResource = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        tv.setOnClickListener(this);
        vp.setOnPageChangeListener(this);
    }

    private void initData() {
        imageViews = new ArrayList<>();
        for (int i = 0; i < imageViewResource.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(imageViewResource[i]);
            imageViews.add(iv);
        }
        adapter = new MyViewPagerAdapter(imageViews);
        vp.setAdapter(adapter);
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.vp);
        tv= (TextView) findViewById(R.id.tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == imageViewResource.length - 1) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
