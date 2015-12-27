package y.Curriculum;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper{//调用父类构造器 
	private static final String DATABASE_NAME="lesson1";
	private static final int DATABASE_VERSION=1;

	public DBHelper(Context ct){//调用Context中的方法创建并打开一个指定名称的数据库对象
		super(ct,DATABASE_NAME,null,DATABASE_VERSION);
		//super(context, name, factory, version);
	}

	/** 
     * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行. 
     * 重写onCreate方法，调用execSQL方法创建表 
     * */ 
	public void onCreate(SQLiteDatabase arg0) {
		
		arg0.execSQL("create table event (day_no integer not null,class_no integer not null,class_name text not null," +
				"class_address text not null,class_week text not null,primary key (day_no,class_no,class_name))");
		
	}

	//当打开数据库时传入的版本号与当前的版本号不同时会调用该方法  
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		

	}
	public Cursor query(String sql, String[] args)//Android查询数据是通过Cursor类来实现
	{
		SQLiteDatabase db = this.getReadableDatabase();//读取数据库
		Cursor cursor = db.rawQuery(sql, args);
		return cursor;
	}
}
