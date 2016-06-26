package com.example.asusk557.mytiku.javaBean;

import java.util.List;

/**
 * Created by Administrator on 2016/2/27.
 */
public class ExamItem {
    private String content;
    private List<ExamSmallItem> list;

    public ExamItem() {

    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ExamSmallItem> getList() {
        return list;
    }

    public void setList(List<ExamSmallItem> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ExamItem{" +
                "content='" + content + '\'' +
                ", list=" + list +
                '}';
    }
}
