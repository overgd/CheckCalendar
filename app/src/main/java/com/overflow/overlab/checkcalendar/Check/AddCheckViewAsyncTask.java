package com.overflow.overlab.checkcalendar.Check;

import android.os.AsyncTask;

import com.overflow.overlab.checkcalendar.CalendarView.CalendarConstraintView;

import java.util.Calendar;

/**
 * Created by over on 11/7/2016.
 */

public class AddCheckViewAsyncTask extends AsyncTask<Integer, Void, Void> {

    private CalendarConstraintView calendarConstraintView;


    public AddCheckViewAsyncTask(CalendarConstraintView calendarConstraintView) {
        super();
        this.calendarConstraintView = calendarConstraintView;
    }

    @Override
    protected Void doInBackground(Integer... params) {

        Calendar calendar;
        calendar = calendarConstraintView.positionCalendar;

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}
