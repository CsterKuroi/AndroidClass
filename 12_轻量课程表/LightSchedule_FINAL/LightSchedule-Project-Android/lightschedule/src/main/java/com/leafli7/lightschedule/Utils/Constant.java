package com.leafli7.lightschedule.Utils;

import android.content.Context;
import android.database.Cursor;

import com.leafli7.lightschedule.Entity.Lesson;
import com.leafli7.lightschedule.Entity.WeekSchedule;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by xxcub on 2015/10/29.
 */
public abstract class Constant {
    /**
     * 原则：程序和数据库中计数全部从０开始
     * 即，程序中的第０周代表数据库（实际上）的第一周．
     */
    public static final String TAG = "leafli7 debug : ";
    public static final String[] DAY_OF_WEEK = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public static Context context;
    public static int DAY_ACCOUNT_OF_ONE_WEEK = 7;
    public static int CURRENT_WEEK = 0;
    public static int CURRENT_DAY_OF_WEEK = 0;
    public static int LESSEN_TIME_ACCOUNT = 6;
    public static int WEEK_NUMS = 20;
    public static String[] LESSON_NUM = {"一", "二", "三", "四", "五", "六"};
    public static String[] LESSON_TIME = {"8:00\n9:45", "10:00\n11:45", "14:00\n15:45", "16:00\n17:45", "18:00\n19:45", "20:00\n21:45"};
    public static final String DbScheduleTableName = "own_schedule";

    public static final String tableColumnId = "id";
    public static final String tableColumnName = "name";
    public static final String tableColumnStartWeek = "start_week";
    public static final String tableColumnEndWeek = "end_week";
    public static final String tableColumnLessonTimeNum = "lesson_time_num";
    public static final String tableColumnTeacherName = "teacher_name";
    public static final String tableColumnClassroom = "classroom";
    public static final String tableColumnIsTinyLesson = "is_tiny_lesson";
    public static final String tableColumnIsFirstHalf = "is_first_half";
    public static final String tableColumnIsSingleWeekLesson = "is_single_week_lesson";
    public static final String tableColumnIsOddWeekLesson = "is_odd_week_lesson";
    public static final String tableColumnDayOfWeek = "day_of_week";


    public static WeekSchedule weekSchedule;
    public static boolean isShowAllLesson;  //是否显示所有课程(非本周课程)

    public static void initial(Context contextPara) {
        context = contextPara;
        initialConfig();
        initialTime();
        initialWeekSchedule();
    }


    public static void initialWeekSchedule() {
        weekSchedule = new WeekSchedule();

        OwnDbHelper ownDbHelper = new OwnDbHelper(context);
        Cursor cursor = ownDbHelper.querySchedule();
        while (cursor.moveToNext()) {
            String[] names = cursor.getColumnNames();
            Lesson lesson = new Lesson();
            for (String nameTmp : names) {
                int index = cursor.getColumnIndex(nameTmp);
                switch (nameTmp){
                    case tableColumnId:lesson.setId(cursor.getInt(index));break;
                    case tableColumnName:lesson.setName(cursor.getString(index));break;
                    case tableColumnStartWeek:lesson.setStartWeek(cursor.getInt(index));break;
                    case tableColumnEndWeek:lesson.setEndWeek(cursor.getInt(index));break;
                    case tableColumnLessonTimeNum:lesson.setLessonTimeNum(cursor.getInt(index));break;
                    case tableColumnTeacherName:lesson.setTeacherName(cursor.getString(index));break;
                    case tableColumnClassroom:lesson.setClassroom(cursor.getString(index));break;
                    case tableColumnIsTinyLesson: {
                        lesson.setIsTinyLesson(cursor.getInt(index) == 1 ? true : false);
                        if (lesson.isTinyLesson()){
                            lesson.setIsFirstHalf(cursor.getInt(cursor.getColumnIndex(tableColumnIsFirstHalf)) == 1 ? true : false);
                        }
                        break;
                    }
                    case tableColumnIsSingleWeekLesson: {
                        lesson.setIsSingleWeekLesson(cursor.getInt(index) == 1 ? true : false);
                        if (lesson.isSingleWeekLesson()){
                            lesson.setIsOddWeekLesson(cursor.getInt(cursor.getColumnIndex(tableColumnIsOddWeekLesson)) == 1 ? true : false);
                        }
                        break;
                    }
                    case tableColumnDayOfWeek:lesson.setDayOfWeek(cursor.getInt(index));break;
                }
            }

            weekSchedule.add(lesson);
        }
    }

    private static void initialConfig() {
        isShowAllLesson = false;
    }

    private static void initialTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int curDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        curDayOfWeek = curDayOfWeek - 1 == 0 ? 6 : curDayOfWeek - 1;
        CURRENT_DAY_OF_WEEK = curDayOfWeek - 1;
    }
}
