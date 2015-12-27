package com.yujiannan.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpenHandler extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    public DBOpenHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("CREATE TABLE message(id INTEGER PRIMARY KEY AUTOINCREMENT,no INTEGER,name VARCHAR,father INTEGER,mother INTEGER,Obrother INTEGER,Osister INTEGER,partner INTEGER,Ybrother INTEGER,Ysister INTEGER,son INTEGER,daughter INTEGER,picture VARCHAR,real_name VARCHAR,tel VARCHAR)");
        init(db);
    }

    private void init(SQLiteDatabase db) {
        insert(db,1,"自己",2,3,4,5,6,7,8,9,10,"","","");
        insert(db,2,"爸爸",0,0,0,0,3,0,0,1,0,"","","");
        insert(db,3,"妈妈",0,0,0,0,2,0,0,1,0,"","","");
        insert(db,4,"哥哥",2,3,0,5,0,1,0,0,0,"","","");
        insert(db,5,"姐姐",2,3,4,0,0,1,0,0,0,"","","");
        insert(db,6,"配偶",2,3,4,5,1,7,8,9,10,"","","");
        insert(db,7,"弟弟",2,3,1,0,0,0,0,0,0,"","","");
        insert(db,8,"妹妹",2,3,1,0,0,0,0,0,0,"","","");
        insert(db,9,"儿子",1,6,0,10,0,0,0,0,0,"","","");
        insert(db,10,"女儿",1,6,9,0,0,0,0,0,0,"","","");
    }
    private void insert(SQLiteDatabase db,int no,String name,int father,int mother,int Obrother,int Osister,int partner,int Ybrother,int Ysister,int son,int daughter,String picture,String real_name,String tel){
        db.execSQL("INSERT INTO message (no,name,father,mother,Obrother,Osister,partner,Ybrother,Ysister,son,daughter,picture,real_name,tel) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{no,name,father,mother,Obrother,Osister,partner,Ybrother,Ysister,son,daughter,picture,real_name,tel});
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
