package com.overflow.overlab.checkcalendar.Check;

import android.os.AsyncTask;

import com.overflow.overlab.checkcalendar.CalendarView.CalendarConstraintView;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsItemsModel;

import java.util.Calendar;

/**
 * Created by over on 11/7/2016.
 */

public class AddCheckViewAsyncTask extends AsyncTask<Integer, Void, Void> {

    CalendarConstraintView calendarConstraintView;
    Calendar calendar;

    public AddCheckViewAsyncTask(CalendarConstraintView calendarConstraintView) {
        super();
        this.calendarConstraintView = calendarConstraintView;
    }

    @Override
    protected Void doInBackground(Integer... params) {

        calendar = calendarConstraintView.positionCalendar;
        CalendarEventsItemsModel checkCalendarEventsItemsModel
                = CheckUtils.loadCheckCalendarEventsItemsModel(calendar);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        calendarConstraintView.addCheckActiveView();
    }
}
