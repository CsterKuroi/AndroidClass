package com.leafli7.lightschedule.Entity;

import com.leafli7.lightschedule.Utils.Constant;

import java.util.ArrayList;

/**
 * Created by xxcub on 2015/10/29.
 */
public class WeekSchedule extends ArrayList<DaySchedule> {

    public WeekSchedule() {
        for (int i = 0; i < Constant.DAY_ACCOUNT_OF_ONE_WEEK; i++) {
            this.add(new DaySchedule());
        }
    }

    public void add(Lesson lesson) {
        this.get(lesson.getDayOfWeek()).add(lesson);
    }

}
