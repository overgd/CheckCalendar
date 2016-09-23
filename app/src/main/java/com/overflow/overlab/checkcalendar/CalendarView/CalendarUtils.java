package com.overflow.overlab.checkcalendar.CalendarView;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by over on 2016-09-23.
 * Utils
 * 7 Days of 1 Week
 *
 */

public class CalendarUtils {

    static final int DAY_OF_WEEK = 7;
    static final int MONTH_OF_YEAR = 12;
    static final int COLUMN_OF_MONTH_VIEW_X = 6;
    static final int COLUMN_OF_MONTH_VIEW_Y = 7; // days of week : 7

    public CalendarUtils(Context context, Calendar calendar) {

    }

    /**
     * Get Now Calendar
     */
    public Calendar getNowCalendar() {
        Calendar nowCalendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        nowCalendar.setTime(date);

        return nowCalendar;
    }

    /**
     * Get Month Number
     * JAN = 0 ~ DEC = 11
     */
    public int GET_MONTH(Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Get Day of Month Number
     */
    public int GET_DAY_OF_MONTH_NUMBER(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get Week of Month Number
     */
    public int GET_WEEK_OF_MONTH_NUMBER(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    /**
     * Get First Day of Week in Month Number
     * SUN = 1 ~ SAT = 7
     */
    public int GET_FIRST_DAY_OF_WEEK_IN_MONTH_NUMBER(Calendar calendar) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(calendar.getTime());
        mCalendar.set(Calendar.DATE, 1); // 1일로 설정
        return mCalendar.get(Calendar.DAY_OF_WEEK);
    }


}
