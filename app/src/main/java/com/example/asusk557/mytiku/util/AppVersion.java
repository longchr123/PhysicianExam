package com.example.asusk557.mytiku.util;
//APP版本信息类

/**
 * Created by Administrator on 2016/2/27.
 */
public class AppVersion {
    private String updateMessage;
    private String apkUrl;
    private int apkCode;

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkCode(int apkCode) {
        this.apkCode = apkCode;
    }

    public int getApkCode() {
        return apkCode;
    }
}
