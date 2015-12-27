package com.leafli7.lightschedule.View;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.lightschedule.R;
import com.leafli7.lightschedule.Entity.Lesson;

/**
 * Created by xxcub on 2015/10/29.
 */
public class LessonItemLayout extends RelativeLayout {
    private Context context;

    private Lesson lesson;
    private int lessonId;

    public LessonItemLayout(Context context, Lesson lesson) {
        super(context);
        this.context = context;
        this.lesson = lesson;
        this.lessonId = lesson.getId();

        LayoutInflater.from(context).inflate(R.layout.lesson_item_layout, this, true);
    }

    public int getLessonId() {
        return lessonId;
    }
}
