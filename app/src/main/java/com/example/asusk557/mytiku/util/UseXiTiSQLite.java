package com.example.asusk557.mytiku.util;
//习题数据库的使用，包括试用和正式的数据库


import com.example.asusk557.mytiku.javaBean.TestItem;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class UseXiTiSQLite {

    //获取数据库中文表名
    public static List<String> getTableChineseName(SQLiteDatabase database) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query("tableNames", new String[]{"EnglishName", "ChineseName"}, null, null, null, null, null, null);
        //利用游标遍历所有数据对象
        while (cursor.moveToNext()) {
            String chineseName = cursor.getString(cursor.getColumnIndex("ChineseName"));
            list.add(chineseName);
        }
        cursor.close();
        return list;
    }
    //获取数据库英文表名
    public static List<String> getTableEnglishName(SQLiteDatabase database) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query("tableNames", new String[]{"EnglishName", "ChineseName"}, null, null, null, null, null, null);
        //利用游标遍历所有数据对象
        while (cursor.moveToNext()) {
            String englishName = cursor.getString(cursor.getColumnIndex("EnglishName"));
            list.add(englishName);
        }
        cursor.close();
        return list;
    }

    //获取数据库某一个表的数据
    public static List<TestItem> GetTableData(SQLiteDatabase database, String tableName) {
        List<TestItem> list = new ArrayList<>();
        Cursor cursor = database.query(tableName, null, null, null, null, null, null, null);
        //利用游标遍历所有数据对象
        while (cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndex("题干"));
            String answerA = cursor.getString(cursor.getColumnIndex("A"));
            String answerB = cursor.getString(cursor.getColumnIndex("B"));
            String answerC = cursor.getString(cursor.getColumnIndex("C"));
            String answerD = cursor.getString(cursor.getColumnIndex("D"));
            String answerE = cursor.getString(cursor.getColumnIndex("E"));
            String answer = cursor.getString(cursor.getColumnIndex("答案"));
            String jieXi = cursor.getString(cursor.getColumnIndex("解析"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TestItem testItem = new TestItem(content, answerA, answerB, answerC, answerD, answerE, answer, jieXi, id);
            list.add(testItem);
        }
        cursor.close();
        return list;
    }
    //创建表格
    public static void creatTable(SQLiteDatabase database1,SQLiteDatabase database2){
        for (int i = 0; i < UseXiTiSQLite.getTableEnglishName(database1).size(); i++) {
            database2.execSQL("create table if not exists " +
                    UseXiTiSQLite.getTableEnglishName(database1).get(i) +
                    "("
                    + "id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + "题干 varchar,"
                    + "A varchar,"
                    + "B varchar,"
                    + "C varchar,"
                    + "D varchar,"
                    + "E varchar,"
                    + "答案 varchar,"
                    + "extension varchar,"
                    + "解析 varchar,"
                    + "testId integer)");
        }
        database1.close();
        database2.close();
    }
}
