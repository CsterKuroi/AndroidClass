package com.leafli7.lightschedule.Entity;

import com.leafli7.lightschedule.Utils.Constant;

import java.util.ArrayList;

/**
 * Created by xxcub on 2015/10/29.
 */
public class DaySchedule extends ArrayList<SingleLessonTime> {

    public DaySchedule(){
        for (int i = 0; i < Constant.LESSEN_TIME_ACCOUNT; i++){
            this.add(new SingleLessonTime());
        }
    }

    public void add(Lesson lesson){
        this.get(lesson.getLessonTimeNum()).add(lesson);
    }

}
