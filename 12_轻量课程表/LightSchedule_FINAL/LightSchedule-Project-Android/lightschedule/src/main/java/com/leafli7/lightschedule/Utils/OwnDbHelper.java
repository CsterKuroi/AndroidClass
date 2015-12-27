package com.leafli7.lightschedule.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.leafli7.lightschedule.Entity.Lesson;

/**
 * Created by xxcub on 2015/10/30.
 */
public class OwnDbHelper extends SQLiteOpenHelper {
    String TAG = Constant.TAG + getClass().getSimpleName();
    private static String dbScheduleTableName = Constant.DbScheduleTableName;
    private static int version = 1;
    private Context context;

    public OwnDbHelper(Context context) {
        super(context, dbScheduleTableName, null, version);
        this.context = context;
//        Log.e(TAG, "sql construct");

//        String sql = "create table if not exists " + dbScheduleTableName + "(" +
//                "id integer primary key AUTOINCREMENT," +
//                "name text not null," +
//                "start_week tinyint not null," +
//                "end_week tinyint not null," +
//                "lesson_time_num tinyint not null," +
//                "teacher_name nchar(55)," +
//                "classroom text not null," +
//                "is_tiny_lesson int not null," +
//                "is_first_half int," +
//                "is_single_week_lesson int not null," +
//                "is_odd_week_lesson int," +
//                Constant.tableColumnDayOfWeek + " int" +
//                ");";
//        getWritableDatabase().execSQL(sql);

//        showAllData();
    }

    //show all date in db
    //for debug
    void showAllData() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from " + dbScheduleTableName + ";";
        Log.e(TAG, "Show all data from database");
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Log.e(TAG, "------");
            String[] names = cursor.getColumnNames();
            for (String name : names) {
                Log.e(TAG, name + " : " + cursor.getString(cursor.getColumnIndex(name)));
            }
            Log.e(TAG, "------");
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//        private int id;
//        private String name;
//        private int startWeek;
//        private int endWeek;
//        private int lessonTimeNum;  //上课节数
//        private String teacherName;
//        private String classroom;
//        private boolean isTinyLesson;   //是否为半节课的小课
//        private boolean isFirstHalf;    //是否为前半小节课 false则为后半节
//        private boolean isSingleWeekLesson; //是否为单双周周课程
//        private boolean isOddWeekLesson; //是否为奇数周课程

        String sql = "create table if not exists " + dbScheduleTableName + "(" +
                "id integer primary key AUTOINCREMENT," +
                "name text not null," +
                "start_week tinyint not null," +
                "end_week tinyint not null," +
                "lesson_time_num tinyint not null," +
                "teacher_name nchar(55)," +
                "classroom text not null," +
                "is_tiny_lesson int not null," +
                "is_first_half int," +
                "is_single_week_lesson int not null," +
                "is_odd_week_lesson int," +
                Constant.tableColumnDayOfWeek + " int" +
                ");";
        db.execSQL(sql);
        Log.e(TAG, "sqlite oncreate");
    }

    /**
     *
     * @param lesson
     * @return  返回插入课程的id
     */
    public int insertLesson(Lesson lesson) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        long rowid = -1;
        try {
            cv.put(Constant.tableColumnName, lesson.getName());
            cv.put(Constant.tableColumnStartWeek, lesson.getStartWeek());
            cv.put(Constant.tableColumnEndWeek, lesson.getEndWeek());
            cv.put(Constant.tableColumnLessonTimeNum, lesson.getLessonTimeNum());
            cv.put(Constant.tableColumnTeacherName, lesson.getTeacherName());
            cv.put(Constant.tableColumnClassroom, lesson.getClassroom());
            cv.put(Constant.tableColumnIsTinyLesson, lesson.isTinyLesson() ? 1 : 0);
            cv.put(Constant.tableColumnIsFirstHalf, lesson.isFirstHalf() ? 1 : 0);
            cv.put(Constant.tableColumnIsSingleWeekLesson, lesson.isSingleWeekLesson() ? 1 : 0);
            cv.put(Constant.tableColumnIsOddWeekLesson, lesson.isOddWeekLesson() ? 1 : 0);
            cv.put(Constant.tableColumnDayOfWeek, lesson.getDayOfWeek());
            rowid = db.insert(dbScheduleTableName, null, cv);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(context, "Wrong input info! Please check!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "wrong input info! Exception!");
            Log.e(TAG, e.toString());
            return -1;
        }

        Cursor c = db.rawQuery("select id from " + dbScheduleTableName + " where id=" + rowid + ";", null);
        c.moveToFirst();
        Log.e(TAG, "return lessonId" + String.valueOf(c.getInt(0)));
        return c.getInt(0);
    }

    public boolean deleteLesson(int lessonId) {
        try {
            String sql = "delete from " + dbScheduleTableName + " where id=" + lessonId + ";";
            getWritableDatabase().execSQL(sql);
            Log.e(TAG, "delete");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "delete item exception!");
            Toast.makeText(context, "delete item exception!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public int updateLesson(Lesson lesson) {
        Log.e(TAG, "updateLesson id " + lesson.getId());
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constant.tableColumnName, lesson.getName());
        cv.put(Constant.tableColumnStartWeek, lesson.getStartWeek());
        cv.put(Constant.tableColumnEndWeek, lesson.getEndWeek());
        cv.put(Constant.tableColumnLessonTimeNum, lesson.getLessonTimeNum());
        cv.put(Constant.tableColumnTeacherName, lesson.getTeacherName());
        cv.put(Constant.tableColumnClassroom, lesson.getClassroom());
        cv.put(Constant.tableColumnIsTinyLesson, lesson.isTinyLesson() ? 1 : 0);
        cv.put(Constant.tableColumnIsFirstHalf, lesson.isFirstHalf() ? 1 : 0);
        cv.put(Constant.tableColumnIsSingleWeekLesson, lesson.isSingleWeekLesson() ? 1 : 0);
        cv.put(Constant.tableColumnIsOddWeekLesson, lesson.isOddWeekLesson() ? 1 : 0);
        cv.put(Constant.tableColumnDayOfWeek, lesson.getDayOfWeek());
        String[] args = {String.valueOf(lesson.getId())};
        int rtn = db.update(dbScheduleTableName, cv, Constant.tableColumnId + "=?", args);
        Log.e(TAG, "update return : " + String.valueOf(rtn));
        return rtn;
    }

    public Cursor querySchedule() {
        String sql = "select * from " + dbScheduleTableName + ";";
        return getWritableDatabase().rawQuery(sql, null);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
