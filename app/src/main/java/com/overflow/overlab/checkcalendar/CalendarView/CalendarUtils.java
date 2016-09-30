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
    public static Calendar getNowCalendar() {
        Calendar nowCalendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        nowCalendar.setTime(date);

        return nowCalendar;
    }

    /**
     * Get Current Year Number
     * 1900년부터 시작해서 현재 년도까지
     * @return ex) 2016 - 1900 = 116
     */
    public static int CURRENT_YEAR_NUMBER() {
        return getNowCalendar().get(Calendar.YEAR) - 1900;
    }

    /**
     * Get Current Month Number
     * @return Jan = 1 ~ DEC = 12
     */
    public static int CURRENT_MONTH_NUMBER() {
        return getNowCalendar().get(Calendar.MONTH) + 1;
    }

    /**
     * Position Month
     * 1900 ~ 2200 사이에 현재 월 위치
     * @return position
     */
    public static int POSITION_CURRENT_MONTH () {
        return (CURRENT_YEAR_NUMBER() * 12) + CURRENT_MONTH_NUMBER();
    }

    public static Calendar CONVERT_MONTH_POSITION_NUMBER_TO_CALENDAR (int positionMonth) {
        int year = (positionMonth / 12) + 1900;
        int month = (positionMonth % 12);
        if(month == 0) {  // 나머지가 0일 경우 12월이며, 년도가 +1되는 걸 막아주어야 한다.
            year = year - 1;
            month = 12;
        }
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month - 1, 1);
        return mCalendar;
    }

    /**
     * Get Month Number
     * JAN = 0 ~ DEC = 11
     */
    public static int GET_MONTH(Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Get Day of Month Number
     */
    public static int GET_NUMBER_DAY_OF_MONTH(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get Week of Month Number
     */
    public static int GET_NUMBER_WEEK_OF_MONTH(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    /**
     * Get First Day of Week in Month Number
     * SUN = 1 ~ SAT = 7
     */
    public static int GET_NUMBER_FIRST_DAY_OF_WEEK_IN_MONTH(Calendar calendar) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(calendar.getTime());
        mCalendar.set(Calendar.DATE, 1); // 1일로 설정
        return mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
    }


}
