package com.example.asusk557.mytiku.util;

import com.example.asusk557.mytiku.javaBean.AppItem;
import com.example.asusk557.mytiku.javaBean.ExamItem;
import com.example.asusk557.mytiku.javaBean.ExamSmallItem;
import com.example.asusk557.mytiku.javaBean.RealTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/27.
 */
public class MyJson {

    public static List<ExamItem> parseJson(String json) {
        List<ExamItem> list = new ArrayList<>();//有多少科目就有多少个对象,比如医师，药师，护理学
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject3 = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonObject3.getJSONArray("content");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String content = jsonObject1.getString("title");
                JSONArray jsonArray1 = jsonObject1.getJSONArray("content");
                ExamItem examItem = new ExamItem();
                examItem.setContent(content);
                List<ExamSmallItem> list1 = new ArrayList<>();//每个科目有多少个分支职业，比如医师中的
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                    String testDBURL = jsonObject2.getString("testDBURL");
                    String testDBSize = jsonObject2.getString("testDBSize");
                    String formalDBURL = jsonObject2.getString("formalDBURL");
                    String formalDBSize = jsonObject2.getString("formalDBSize");
                    String title = jsonObject2.getString("title");
                    String id = jsonObject2.getString("id");
                    ExamSmallItem examSmallItem = new ExamSmallItem();
                    examSmallItem.setTitle(title);
                    examSmallItem.setTestDBURL(testDBURL);
                    examSmallItem.setTestDBSize(testDBSize);
                    examSmallItem.setFormalDBURL(formalDBURL);
                    examSmallItem.setFormalDBSize(formalDBSize);
                    examSmallItem.setId(id);
                    list1.add(examSmallItem);
                }
                examItem.setList(list1);
                list.add(examItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static List<RealTime> parseJsonReal(String json) {
        List<RealTime> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.getString("title");
                String teacher = jsonObject1.getString("teacher");
                String date = jsonObject1.getString("date");
                String pic = jsonObject1.getString("pic");
                String num = jsonObject1.getString("num");
                String yid = jsonObject1.getString("yid");
                RealTime realTime = new RealTime();
                realTime.setTitle(title);
                realTime.setDate(date);
                realTime.setNum(num);
                realTime.setTeacher(teacher);
                realTime.setPic(pic);
                realTime.setYid(yid);
                list.add(realTime);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<AppItem> parseJsonApp(String json) {
        List<AppItem> list = new ArrayList<>();
        try {
            String content=json.substring(json.indexOf("{"));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray jsonArray = jsonObject.getJSONArray("contents");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.getString("title");
                String ad_url = jsonObject1.getString("ad_url");
                String img = jsonObject1.getString("img");
                AppItem appItem = new AppItem();
                appItem.setTitle(title);
                appItem.setAppUrl(ad_url);
                appItem.setImageUrl(img);
                list.add(appItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
