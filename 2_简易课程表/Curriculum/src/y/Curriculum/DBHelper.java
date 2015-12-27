package y.Curriculum;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper{//���ø��๹���� 
	private static final String DATABASE_NAME="lesson1";
	private static final int DATABASE_VERSION=1;

	public DBHelper(Context ct){//����Context�еķ�����������һ��ָ�����Ƶ����ݿ����
		super(ct,DATABASE_NAME,null,DATABASE_VERSION);
		//super(context, name, factory, version);
	}

	/** 
     * �����ݿ��״δ���ʱִ�и÷�����һ�㽫������ȳ�ʼ���������ڸ÷�����ִ��. 
     * ��дonCreate����������execSQL���������� 
     * */ 
	public void onCreate(SQLiteDatabase arg0) {
		
		arg0.execSQL("create table event (day_no integer not null,class_no integer not null,class_name text not null," +
				"class_address text not null,class_week text not null,primary key (day_no,class_no,class_name))");
		
	}

	//�������ݿ�ʱ����İ汾���뵱ǰ�İ汾�Ų�ͬʱ����ø÷���  
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		

	}
	public Cursor query(String sql, String[] args)//Android��ѯ������ͨ��Cursor����ʵ��
	{
		SQLiteDatabase db = this.getReadableDatabase();//��ȡ���ݿ�
		Cursor cursor = db.rawQuery(sql, args);
		return cursor;
	}
}
