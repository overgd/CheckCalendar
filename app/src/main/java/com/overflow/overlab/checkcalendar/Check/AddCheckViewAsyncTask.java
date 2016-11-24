package com.overflow.overlab.checkcalendar.Check;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.overflow.overlab.checkcalendar.CalendarView.CalendarConstraintView;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarDayTextView;
import com.overflow.overlab.checkcalendar.R;

import java.util.Calendar;

/**
 * Created by over on 11/7/2016.
 */

public class AddCheckViewAsyncTask extends AsyncTask<Integer, Void, CalendarDayTextView[][]> {

    private CalendarConstraintView calendarConstraintView;


    public AddCheckViewAsyncTask(CalendarConstraintView calendarConstraintView) {
        super();
        this.calendarConstraintView = calendarConstraintView;
    }

    @Override
    protected CalendarDayTextView[][] doInBackground(Integer... params) {

        Calendar calendar;
        calendar = calendarConstraintView.positionCalendar;
        CalendarDayTextView[][] calendarDayTextViews = calendarConstraintView.calendarDayTextView;

        return calendarDayTextViews;
    }

    @Override
    protected void onPostExecute(CalendarDayTextView[][] calendarDayTextViews) {
        super.onPostExecute(calendarDayTextViews);
        int[] posmain = new int[2];
        calendarConstraintView.getLocationInWindow(posmain);

        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 7; j++) {
                int[] posText = new int[2];
                calendarDayTextViews[i][j].getLocationInWindow(posText);
                Log.d("position x", String.valueOf(posText[0]));
                Log.d("position x", String.valueOf(posText[1]));
                ImageView view = new ImageView(calendarConstraintView.getContext());
                view.setImageDrawable(calendarConstraintView.getContext().getResources().getDrawable(R.drawable.ic_check_black_24dp));
                view.setX(posText[0]-posmain[0]);
                view.setY(posText[1]-posmain[1]);
                calendarConstraintView.addView(view);
            }
        }

//        Log.d("position x", String.valueOf(calendarConstraintView.getX()));
//        Log.d("position y", String.valueOf(calendarConstraintView.getY()));


    }
}
