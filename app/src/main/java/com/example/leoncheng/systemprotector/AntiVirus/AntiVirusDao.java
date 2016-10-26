package com.example.leoncheng.systemprotector.AntiVirus;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/10/18.
 */
public class AntiVirusDao {
    public static String checkVirus(String md5) {
        String desc = null;
        SQLiteDatabase db=SQLiteDatabase.openDatabase("/data/data/com.example.leoncheng.systemprotector.AntiVirus/antivirus.db",null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor=db.rawQuery("select desc from datable where md5=?",new String[]{md5});
        if(cursor.moveToNext()){
            desc=cursor.getString(0);
        }
        cursor.close();
        db.close();
        return desc;
    }

}
