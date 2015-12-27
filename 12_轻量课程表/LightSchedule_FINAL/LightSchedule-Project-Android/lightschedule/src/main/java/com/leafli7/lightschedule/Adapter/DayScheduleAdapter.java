package com.leafli7.lightschedule.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lightschedule.R;
import com.leafli7.lightschedule.Activity.AddLessonActivity;
import com.leafli7.lightschedule.Entity.Lesson;
import com.leafli7.lightschedule.Entity.SingleLessonTime;
import com.leafli7.lightschedule.Utils.Constant;
import com.leafli7.lightschedule.Utils.OwnDbHelper;
import com.leafli7.lightschedule.View.LessonItemLayout;

import java.util.ArrayList;

/**
 * Created by xxcub on 2015/10/29.
 */
public class DayScheduleAdapter extends BaseAdapter {
    private String TAG = Constant.TAG + getClass().getSimpleName();

    private Context context;
    private ArrayList<SingleLessonTime> singleLessonTimes = new ArrayList<>();
    private int curAdapterDayOfWeek;

    public DayScheduleAdapter(Context context, int curAdapterDayOfWeek){
        this.context = context;
        this.curAdapterDayOfWeek = curAdapterDayOfWeek;
        singleLessonTimes = Constant.weekSchedule.get(curAdapterDayOfWeek);
    }


    @Override
    public int getCount() {
        return singleLessonTimes.size();
    }

    @Override
    public Object getItem(int position) {
        return singleLessonTimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String[] lessonNum = Constant.LESSON_NUM;
        String[] lessonTime = Constant.LESSON_TIME;

        LinearLayout llMain, llLessonTimeNum;
        llMain = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.day_schedule_list_item, null);
        llLessonTimeNum = (LinearLayout) llMain.findViewById(R.id.llLessonTimeNum);
        llLessonTimeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked LessonTimeNum " + position, Toast.LENGTH_SHORT).show();
            }
        });
        //TODO : 长按添加课程
        llLessonTimeNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(context, AddLessonActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AddLessonActivity.MODE, AddLessonActivity.ADD_MODE);
                bundle.putInt("dayOfWeek", curAdapterDayOfWeek);
                bundle.putInt("lessonTimeNum", position);
                i.putExtras(bundle);
                context.startActivity(i);
                Log.e(TAG, "leafli : wait for add activity!");
                Toast.makeText(context, "long clicked LessonTimeNum " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        LinearLayout llSingleLessonTime = (LinearLayout) llMain.findViewById(R.id.llSingleLessonTime);
        ArrayList<Lesson> curLessones = singleLessonTimes.get(position);
        for (final Lesson lesson:curLessones) {
            final LessonItemLayout lessonItemLayout = new LessonItemLayout(context, lesson);
            ((TextView)lessonItemLayout.findViewById(R.id.tvLessonName)).setText(lesson.getName());
            ((TextView)lessonItemLayout.findViewById(R.id.tvLessonClassroom)).setText(lesson.getClassroom());
            if (lesson.isTinyLesson()){
                TextView tvTinyLesson = (TextView) lessonItemLayout.findViewById(R.id.tvTinyLesson);
                tvTinyLesson.setVisibility(View.VISIBLE);
                tvTinyLesson.setText(lesson.isFirstHalf() ? "前" : "后");
            }
            if (lesson.isSingleWeekLesson()){
                TextView tvSingleWeekLesson = (TextView) lessonItemLayout.findViewById(R.id.tvSingleWeekLesson);
                tvSingleWeekLesson.setVisibility(View.VISIBLE);
                tvSingleWeekLesson.setText(lesson.isOddWeekLesson() ? "单" : "双");
            }
            lessonItemLayout.setFocusable(true);
            //TODO : 单击课程查看
            lessonItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, AddLessonActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(AddLessonActivity.MODE, AddLessonActivity.VIEW_MODE);
                    bundle.putParcelable("lesson", lesson);
                    i.putExtras(bundle);
                    context.startActivity(i);
                    Toast.makeText(context, "clicked lesson : " + lessonItemLayout.getLessonId(), Toast.LENGTH_SHORT).show();
                }
            });
            //长按课程删除
            lessonItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "long clicked lesson : " + lessonItemLayout.getLessonId(), Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(context).setTitle("Del Lesson").setMessage("Confirm to delete the lesson?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OwnDbHelper dbHelper = new OwnDbHelper(context);
                                    dbHelper.deleteLesson(lessonItemLayout.getLessonId());
                                    ArrayList<Lesson> lessons = Constant.weekSchedule.get(lesson.getDayOfWeek()).get(lesson.getLessonTimeNum());
                                    lessons.remove(lesson);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    return false;
                }
            });
            llSingleLessonTime.addView(lessonItemLayout);
        }


//        lessonItemLayout = new LessonItemLayout(context, 123);
//        lessonItemLayout.setFocusable(true);
//        llSingleLessonTime.addView(lessonItemLayout);

        TextView tvLessonNum = (TextView) llMain.findViewById(R.id.tvLessonNum);
        TextView tvLessonTime = (TextView) llMain.findViewById(R.id.tvLessonTime);

        tvLessonNum.setText(lessonNum[position]);
        tvLessonTime.setText(lessonTime[position]);

        return llMain;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
