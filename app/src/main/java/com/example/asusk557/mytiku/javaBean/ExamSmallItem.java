package com.example.asusk557.mytiku.javaBean;

/**
 * Created by Administrator on 2016/2/27.
 */
public class ExamSmallItem {
    private String testDBURL;
    private String testDBSize;
    private String formalDBURL;
    private String formalDBSize;
    private String title;
    private String id;

    public ExamSmallItem() {

    }

    public String getTestDBURL() {
        return testDBURL;
    }

    public void setTestDBURL(String testDBURL) {
        this.testDBURL = testDBURL;
    }

    public String getTestDBSize() {
        return testDBSize;
    }

    public void setTestDBSize(String testDBSize) {
        this.testDBSize = testDBSize;
    }

    public String getFormalDBURL() {
        return formalDBURL;
    }

    public void setFormalDBURL(String formalDBURL) {
        this.formalDBURL = formalDBURL;
    }

    public String getFormalDBSize() {
        return formalDBSize;
    }

    public void setFormalDBSize(String formalDBSize) {
        this.formalDBSize = formalDBSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
