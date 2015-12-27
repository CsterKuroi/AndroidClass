package com.example.wei.spark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by 64932 on 2015/12/12.
 */
public class PostDBHelper extends SQLiteOpenHelper{

    private String id = "_id";
    private String table_name = "post_table";
    private String content_name = "content_name";
    private String content = "content";
    private String picture_name = "picture_name";
    private String picture = "picture";

    public PostDBHelper(Context context, String  name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase arg0){
        arg0.execSQL("create table if not exists " +
                table_name + "(" +
                id + " integer primary key, " +
                content_name + " varchar(100), " +
                content + " varchar(1000), " +
                picture_name + " varchar(100), " +
                picture + " blob)");
    }

    public boolean addPost(String the_user_name, String the_content){ //发表一段文字
        try{
            ContentValues cv = new ContentValues();
            cv.put(content_name, the_user_name);
            cv.put(content, the_content);
            cv.put(picture_name, "");
            this.getWritableDatabase().insert(table_name, null, cv);
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean setImg(int id, Bitmap bitmap, String the_picture_name){ //将某个图片与某段文字绑定
        ContentValues cv = new ContentValues();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        cv.put(picture, os.toByteArray());
        cv.put(picture_name, the_picture_name);
        this.getWritableDatabase().update(table_name, cv, "_id = " + id, null);
        return true;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public void close(){
        this.getWritableDatabase().close();
    }



}
