package com.overflow.overlab.checkcalendar.Check;

import android.os.AsyncTask;
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

        return calendarConstraintView.calendarDayTextView;
    }

    @Override
    protected void onPostExecute(CalendarDayTextView[][] calendarDayTextViews) {
        super.onPostExecute(calendarDayTextViews);

        int[] posMain = new int[2];
        int[] posText = new int[2];

        calendarConstraintView.getLocationInWindow(posMain);
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 7; j++) {

                calendarDayTextViews[i][j].getLocationInWindow(posText);
                if(posText[0]-posMain[0] == 0) {
                    break;
                }
                ImageView checkImageView = new ImageView(calendarConstraintView.getContext());
                checkImageView.setImageDrawable(calendarConstraintView.getContext().getResources().getDrawable(R.drawable.ic_check_black_24dp));
                checkImageView.setX(posText[0] - posMain[0]);
                checkImageView.setY(posText[1] - posMain[1]);
                calendarConstraintView.checkImageView[i][j] = checkImageView;
                calendarConstraintView.addView(calendarConstraintView.checkImageView[i][j]);

            }
        }

    }
}
