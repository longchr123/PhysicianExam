package com.example.asusk557.mytiku.util;
//错题数据库的使用

import com.example.asusk557.mytiku.javaBean.TestItem;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class UseCuoTiSQLite {
    public static SQLiteDatabase database;

    public static List<TestItem> GetTableData(String tableName) {
        List<TestItem> list = new ArrayList<>();
        database=SQLiteDatabase.openOrCreateDatabase(DownLoadSQLite.cuoTiFilename,ConfigUtil.mi_ma,null);
        Cursor cursor = database.query(tableName, new String[]{"id", "题干", "A", "B", "C", "D", "E", "答案", "解析", "testId"}, null, null, null, null, null, null);
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
        database.close();
        cursor.close();
        return list;
    }

}
