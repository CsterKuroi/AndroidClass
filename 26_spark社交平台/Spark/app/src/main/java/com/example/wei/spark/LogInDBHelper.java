package com.example.wei.spark;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 64932 on 2015/12/12.
 */
public class LogInDBHelper extends SQLiteOpenHelper {

    private String id = "_id";
    private String table_name = "login_table";
    private String user_name = "user_name";
    private String password = "password";

    public LogInDBHelper(Context context, String  name, CursorFactory factory, int version){
        super(context, name, factory, version);
        this.getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase arg0){
        arg0.execSQL("create table if not exists " +
                table_name + "(" +
                id + " integer primary key, " +
                user_name + " varchar(100), " +
                password + " varchar(20))");
    }

    public boolean addUser(String the_user_name, String the_password){ //添加用户
        try{
            ContentValues cv = new ContentValues();
            cv.put(user_name, the_user_name);
            cv.put(password, the_password);
            this.getWritableDatabase().insert(table_name, null, cv);
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public void close(){
        this.getWritableDatabase().close();
    }
}
