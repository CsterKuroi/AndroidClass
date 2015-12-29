/**
 * 
 */
package com.example.keepfit;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

/**
 * @author Cathy M
 *
 */
enum AimChoice{
	STRONG,SLIM,POWER,LFAT;
}
enum SexChoice{
	ALL,MAN,WOMEN;
}
enum PartChoice{
	ALL,BREST,STOMACH,BACK,SHOULDER,LEG,ARM;
}
enum HardChoice{
	ALL,HARD,MEDIUM,EASY;
}


public class DBHelper{
	public static final String DATABASE_NAME = "keepfit.db";
	public static final int DATABASE_VERSION = 4;
	public static final String COURSE_TABLE_NAME = "course";
	public static final String USER_TABLE_NAME = "user";
	public static final String SELECT_TABLE_NAME = "sc";

	public static final String C_ID = "_id";
	public static final String C_NAME = "c_name";
	public static final String C_DESP = "c_desp";
	public static final String C_PIC = "c_pic";
	public static final String C_ADR_HOME = "c_adr_home";
	public static final String C_ADR_WEB = "c_adr_web";
	public static final String C_AIM = "c_aim";
	public static final String C_PART = "c_part";
	public static final String C_HARD = "c_hard";
	public static final String C_MIN = "c_min";
	public static final String C_SEX = "c_sex";
	
	public static final String U_ID = "_id";
	public static final String U_NAME = "u_name";
	public static final String U_SEX = "u_sex";
	public static final String U_WEIGHT = "u_weight";
	public static final String U_HEIGHT = "u_height";
	public static final String U_KEY = "u_key";
	
	public static final String S_ID = "_id";
	public static final String SC_ID = "sc_name";
	public static final String S_DAY = "s_day";
	public static final String S_ALARM = "s_alarm";
	public static final String S_NO_ALARM = "您还没有设置";
	//具体格式 String tmpS=format(hourOfDay)+"："+format(minute);
	
//	private static final String TAG = "DBHelper";
	private static OpenHelper mDbHelper;
	private static SQLiteDatabase mDb;
	private final Context mCtx;

	
	private static class OpenHelper extends SQLiteOpenHelper{
		public OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "create table "+COURSE_TABLE_NAME+"("
						+C_ID+" integer primary key autoincrement,"
						+C_NAME+" text not null,"
						+C_DESP+" text not null,"
						+C_PIC+" integer not null,"
						+C_ADR_HOME+" text,"						
						+C_ADR_WEB+" text not null,"
						+C_AIM+" text not null,"
						+C_PART+" text not null,"
						+C_HARD+" text not null,"
						+C_SEX+" text not null,"
						+C_MIN+" integer not null"
						+");";
			
			db.execSQL(sql);
			Log.i("adm:createDB=", sql);
			sql = "create table "+USER_TABLE_NAME+"("
					+U_ID+" integer primary key autoincrement,"
					+U_NAME+" text not null,"
					+U_SEX+" text not null,"
					+U_WEIGHT+" text not null,"
					+U_HEIGHT+" text not null,"				
					+U_KEY+" text not null"
					+");";
			
			db.execSQL(sql);
			Log.i("adm:createDB=", sql);
			
			sql = "create table "+SELECT_TABLE_NAME+"("
					+S_ID+" integer primary key autoincrement,"
					+SC_ID+" integer not null,"
					+S_DAY+" integer not null ,"
					+S_ALARM+" text not null,"
					+"foreign key ("+SC_ID+ ") references "
							+COURSE_TABLE_NAME +" (_id)"
					+");";
			
			db.execSQL(sql);
			Log.i("adm:createDBttttt=", sql);

		}


		
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists "+COURSE_TABLE_NAME+";");
			db.execSQL("drop table if exists "+USER_TABLE_NAME+";");
			db.execSQL("drop table if exists "+SELECT_TABLE_NAME+";");
			onCreate(db);
		}
	}

	
	public DBHelper(Context ctx) {
		this.mCtx = ctx;
	}
	
	public DBHelper open() throws SQLException {
		if (mDbHelper == null) {
			mDbHelper = new OpenHelper(mCtx);
		}
		if (mDb == null && mDbHelper!= null) {
			mDb = mDbHelper.getWritableDatabase();
		}
		
		
		return this;
	}

	public  void insertInit() {
	//	mDb.delete(USER_TABLE_NAME, null, null);
	//	mDb.delete(COURSE_TABLE_NAME, null, null);
		
		createUser(mDb, "您还没有设置", "您还没有设置", "您还没有设置", "您还没有设置", "您还没有设置");
		createCourse(mDb,"家庭臂部训练", "方便您在家中塑形，每次共21个动作",
				R.drawable.bi_xiao ,"", "www.what.com",
	    		AimChoice.STRONG.toString(), PartChoice.ARM.toString(),
	    		HardChoice.EASY.toString(), SexChoice.WOMEN.toString(),15);
		createCourse(mDb,"背部拉伸训练", "针对久坐人群，缓解肩颈酸痛，驼背的日常方案，建议每天2-3次",
				R.drawable.bei_xiao ,"", "www.what.com",
	    		AimChoice.STRONG.toString(), PartChoice.BACK.toString(),
	    		HardChoice.EASY.toString(), SexChoice.WOMEN.toString(), 10); 
		createCourse(mDb,"两周腿部训练", "适合于想塑造腿部线条的新手，建议采用本课程一周训练1-2次，持续6周以上",
				R.drawable.tui_xiao ,"leg.mp4", "www.what.com",
	    		AimChoice.STRONG.toString(),  PartChoice.LEG.toString(),
	    		HardChoice.EASY.toString(), SexChoice.WOMEN.toString(),5);  
		createCourse(mDb,"三周腰部训练", "腰腹部塑形。针对现代女性普遍存在的小腹突出，腰椎前凸，盆骨前倾等问题。建议使用本课程帮助解决以上问题，每周进行3-5次训练。",
				R.drawable.yao_xiao ,"waist.mp4", "www.what.com",
	    		AimChoice.STRONG.toString(), PartChoice.STOMACH.toString(),
	    		HardChoice.EASY.toString(),SexChoice.WOMEN.toString(),  10);  
		createCourse(mDb,"男士背部训练", "适合于想塑造背部线条的新手，建议采用本课程一周训练1-2次，持续6周以上",
				R.drawable.nan_bei_xiao ,"", "www.what.com",
	    		AimChoice.STRONG.toString(), PartChoice.BACK.toString(),
	    		HardChoice.EASY.toString(),SexChoice.MAN.toString(), 10);  
		createCourse(mDb,"男士腰部训练", "适合于想塑造腰部线条的新手，建议采用本课程一周训练1-2次，持续6周以上",
				R.drawable.nan_yao_xiao ,"", "www.what.com",
	    		AimChoice.STRONG.toString(),  PartChoice.STOMACH.toString(),
	    		HardChoice.EASY.toString(),SexChoice.MAN.toString(), 10);  
		
	}
	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	public long createUser(SQLiteDatabase db, String u_name, String u_sex,
			String u_weight, String u_height, String u_key) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(U_NAME, u_name);
		initialValues.put(U_SEX, u_sex);
		initialValues.put(U_HEIGHT, u_height);
		initialValues.put(U_WEIGHT, u_weight);
		initialValues.put(U_KEY, u_key);
		

		return db.insert(USER_TABLE_NAME, null, initialValues);
		
		 
	}

	public long createCourse(SQLiteDatabase db,String c_name,String c_desp,int c_pic,
			String c_adr_home,String c_adr_web,
			String c_aim,String c_part,
			String c_hard,String c_sex, int c_min) {
	
		ContentValues initialValues = new ContentValues();

		initialValues.put(C_NAME, c_name);
		initialValues.put(C_PIC, c_pic);  
		initialValues.put(C_DESP, c_desp);
		initialValues.put(C_ADR_HOME, c_adr_home);
		initialValues.put(C_ADR_WEB, c_adr_web);
		initialValues.put(C_AIM, c_aim);
		initialValues.put(C_PART, c_part);
		initialValues.put(C_HARD, c_hard);
		initialValues.put(C_SEX, c_sex);
		
		if(c_min >60 || c_min <0 )
			return -1;
		else {
			initialValues.put(C_MIN, c_min);
		}
		
		return db.insert(COURSE_TABLE_NAME, null, initialValues);
	}

	
	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	public void deleteSelect(String courseIdString) {
		mDb.delete(SELECT_TABLE_NAME,SC_ID+" = "+courseIdString ,null);
		Log.i("adm:", "delete courseSelect : "+courseIdString);
	}

	public void close() {
		mDb.close();
		mDbHelper.close();
	}

	public long updateUser(String colString,String value){
		ContentValues cv = new ContentValues();
	    cv.put(colString, value);
	
		return mDb.update(USER_TABLE_NAME, cv, null, null);
	}

	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	public void updateAlarm(int courseId, String set) {
		ContentValues initialValues = new ContentValues();
	
		initialValues.put(S_ALARM,set);
		mDb.update(SELECT_TABLE_NAME, initialValues, SC_ID+" = "+courseId, null);
		
	}

	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	public void updateDay(int courseId, String dayValue) {
		ContentValues initialValues = new ContentValues();
	
		initialValues.put(S_DAY,dayValue);
		mDb.update(SELECT_TABLE_NAME, initialValues, SC_ID+" = "+courseId, null);
	}

	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	public void selectCourse(int courseId) {
		ContentValues initialValues = new ContentValues();
	
		initialValues.put(SC_ID,courseId);
		initialValues.put(S_DAY,0);  
		initialValues.put(S_ALARM,S_NO_ALARM);
		
		 mDb.insert(SELECT_TABLE_NAME, null, initialValues);
		String sql = "inset into sc which sc_id = "+courseId;
	
		Log.i("adm:", sql);
	}

	public HashMap<String,Object> getUser() {
		Cursor mCursor =
				mDb.query(USER_TABLE_NAME, new String[] { 
						U_NAME,U_SEX,U_WEIGHT,U_HEIGHT}, null, null, null,
						null, null);
		mCursor.moveToFirst();
		if (mCursor.isAfterLast()) {
			return null;
		}
		
		int nameColumn = mCursor.getColumnIndex(U_NAME);
		int sexColumn = mCursor.getColumnIndex(U_SEX);
		int weightColumn = mCursor.getColumnIndex(U_WEIGHT);
		int heightColumn = mCursor.getColumnIndex(U_HEIGHT);
		String curname = mCursor.getString(nameColumn);
		String cursex = mCursor.getString(sexColumn);
		String curweight = mCursor.getString(weightColumn);
		String curheight = mCursor.getString(heightColumn);
		
		HashMap<String,Object> retmap = new HashMap<String,Object>();
		retmap.put(U_NAME, curname);
		retmap.put(U_SEX, cursex);
		retmap.put(U_HEIGHT, curheight);
		retmap.put(U_WEIGHT, curweight);
		
		return retmap;
		
	}

	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	public HashMap<String,Object> getCourse(String id) {
		Cursor mCursor =
				mDb.query(COURSE_TABLE_NAME, new String[] { 
						C_PIC,C_ADR_HOME,C_DESP}, C_ID +" = "+id , null, null,
						null, null);
		mCursor.moveToFirst();
		int picColumn = mCursor.getColumnIndex(C_PIC);
		int adrhomeColumn = mCursor.getColumnIndex(C_ADR_HOME);
		int despColumn = mCursor.getColumnIndex(C_DESP);
		
		int curpic = mCursor.getInt(picColumn);
		String curhome = mCursor.getString(adrhomeColumn);
		String curdesp = mCursor.getString(despColumn);
		
		HashMap<String,Object> retmap = new HashMap<String,Object>();
		retmap.put(C_PIC, curpic+"");
		retmap.put(C_ADR_HOME, curhome);
		retmap.put(C_DESP, curdesp);
		
		return retmap;
		
	}
	
	public HashMap<String,Object> getSelect(int id) {
		HashMap<String,Object> retmap = null;
		Cursor mCursor =
				mDb.query(SELECT_TABLE_NAME, new String[] { 
						S_DAY,S_ALARM}, SC_ID +" = "+id , null, null,
						null, null);
		mCursor.moveToFirst();
		if (mCursor.isAfterLast()) {
			return null;
		}
		else {		
			int dayColumn = mCursor.getColumnIndex(S_DAY);
			int alarmColumn = mCursor.getColumnIndex(S_ALARM);
			int curday = mCursor.getInt(dayColumn);
			String curalarm = mCursor.getString(alarmColumn);
			
			retmap = new HashMap<String,Object>();
			retmap.put(S_DAY,curday+"");
			retmap.put(S_ALARM,curalarm);
						
		}
		return retmap;
	}

	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	public ArrayList<HashMap<String, Object>> getSelectCourse() {
		Cursor mCursor =

				mDb.query(SELECT_TABLE_NAME, new String[] { SC_ID}, null, null, null,
						null, null, null);
				ArrayList<HashMap<String, Object>> retList = new ArrayList<HashMap<String,Object>>();
				for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext())
				{
					HashMap<String, Object> row = new HashMap<String, Object>();
					int scidColumn = mCursor.getColumnIndex(SC_ID);
					int curscid = mCursor.getInt(scidColumn);
					
					row = getCourse(curscid+"");
					row.put(C_ID, curscid);
					
					retList.add(row);
				}
		return retList;
	}

	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	public ArrayList<HashMap<String, Object>> search(Object[] searchBy) {
		String whereString = "";
		if (! searchBy[0].equals(SexChoice.ALL)) {
			whereString = C_SEX+" = \""+searchBy[0].toString()+"\"";
		}
		if (! searchBy[1].equals(PartChoice.ALL)) {
			if (!whereString.equals("") ) {
				whereString = whereString+" and ";
			}
			whereString = whereString+C_PART+" = \""+searchBy[1].toString()+"\"";
		}
		if (! searchBy[2].equals(HardChoice.ALL)) {
			if (!whereString.equals("") ) {
				whereString = whereString+" and ";
			}
			whereString = whereString+C_HARD+" = \""+searchBy[2].toString()+"\"";
		}
		
		Cursor mCursor =
	
		mDb.query(COURSE_TABLE_NAME, new String[] { C_ID,
				C_PIC}, whereString, null, null,
				null, null);
		ArrayList<HashMap<String, Object>> retList = new ArrayList<HashMap<String,Object>>();
		for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext())
		{
			HashMap<String, Object> row = new HashMap<String, Object>();
			int idColumn = mCursor.getColumnIndex(C_ID);
			int picColumn = mCursor.getColumnIndex(C_PIC);
		
			int curid = mCursor.getInt(idColumn);
			int curpic = mCursor.getInt(picColumn);
			
			row.put(C_ID, curid+"");
			row.put(C_PIC, curpic);
			
			retList.add(row);
		}
		
		return retList;  
	}

	public ArrayList<HashMap<String, Object>> getAllCourse() throws SQLException {
	
		Cursor mCursor =
	
		mDb.query(COURSE_TABLE_NAME, new String[] { C_ID,
				C_PIC}, null, null, null,
				null, null, null);
		ArrayList<HashMap<String, Object>> retList = new ArrayList<HashMap<String,Object>>();
		for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext())
		{
			HashMap<String, Object> row = new HashMap<String, Object>();
			int idColumn = mCursor.getColumnIndex(C_ID);
			int picColumn = mCursor.getColumnIndex(C_PIC);
		
			int curid = mCursor.getInt(idColumn);
			int curpic = mCursor.getInt(picColumn);
			
			row.put(C_ID, curid+"");
			row.put(C_PIC, curpic);
			
			retList.add(row);
		}
		
		return retList;  
	}

	
	
}
