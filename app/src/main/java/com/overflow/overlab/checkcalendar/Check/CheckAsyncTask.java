package com.overflow.overlab.checkcalendar.Check;

import android.os.AsyncTask;

import com.overflow.overlab.checkcalendar.CalendarView.CalendarView;

/**
 * Created by over on 11/7/2016.
 */

public class CheckAsyncTask extends AsyncTask<Integer, Void, Void> {

    CalendarView calendarView;

    public CheckAsyncTask(CalendarView calendarView) {
        super();
        this.calendarView = calendarView;
    }

    @Override
    protected Void doInBackground(Integer... params) {

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        calendarView.addCheckActivateView();
    }
}
