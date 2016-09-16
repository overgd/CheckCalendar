package com.overflow.overlab.checkcalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;

/**
 * Created by over on 7/1/2016.
 */
public class CalendarPagesAdapter extends FragmentStatePagerAdapter {

    Context context;
    Calendar selectedCalendar;
    private int YEAR;
    private int MONTH;

    public CalendarPagesAdapter(FragmentManager fm, Context context) {

        super(fm);
        this.context = context;
        selectedCalendar = Calendar.getInstance();

    }

    @Override
    public Fragment getItem(int position) {

        CalendarViewFragment fragment = new CalendarViewFragment();

        Bundle args = new Bundle();

        YEAR = (position / 12) + 1900; // 1900년부터 시작해서 선택된 년도
        MONTH = (position % 12); // 선택된 월
        if(MONTH == 0) {  // 나머지가 0일 경우 12월이며, 년도가 +1되는 걸 막아주어야 한다.
            YEAR = YEAR - 1;
            MONTH = 12;
        }

        selectedCalendar.set(YEAR, MONTH-1, selectedCalendar.get(Calendar.DATE));

        args.putLong(CalendarViewFragment.CALENDAR_TIMEINMILLIS, selectedCalendar.getTimeInMillis());

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return 4400;
    } // 1900년 ~ 2100년까지 월 갯수

    @Override
    public CharSequence getPageTitle(int position) {
        int mon = position % 12;
        if(mon == 0) mon = 12;
        return context.getResources().getStringArray(R.array.calendar_month)[mon-1];
    }



}
