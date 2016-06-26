package com.example.asusk557.mytiku.javaBean;

import java.io.Serializable;

/**
 * Created by ASUSK557 on 2016/2/18.
 */
public class TestItem implements Serializable {
    private String 题干;
    private String A;
    private String B;
    private String C;
    private String D;
    private String E;
    private String 答案;
    private String 解析;
    private int id;

    public TestItem(String 题干, String a, String b, String c, String d, String e, String 答案, String 解析, int id) {
        this.题干 = 题干;
        A = a;
        B = b;
        C = c;
        D = d;
        E = e;
        this.答案 = 答案;
        this.解析 = 解析;
        this.id = id;
    }

    public String get题干() {
        return 题干;
    }

    public void set题干(String 题干) {
        this.题干 = 题干;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getE() {
        return E;
    }

    public void setE(String e) {
        E = e;
    }

    public String get答案() {
        return 答案;
    }

    public void set答案(String 答案) {
        this.答案 = 答案;
    }

    public String get解析() {
        return 解析;
    }

    public void set解析(String 解析) {
        this.解析 = 解析;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "TestItem{" +
                "题干='" + 题干 + '\'' +
                ", A='" + A + '\'' +
                ", B='" + B + '\'' +
                ", C='" + C + '\'' +
                ", D='" + D + '\'' +
                ", E='" + E + '\'' +
                ", 答案='" + 答案 + '\'' +
                ", 解析='" + 解析 + '\'' +
                ", id=" + id +
                '}';
    }
}
