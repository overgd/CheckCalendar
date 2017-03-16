package com.overflow.overlab.checkcalendar.Check;

import android.os.AsyncTask;
import android.widget.ImageView;

import com.overflow.overlab.checkcalendar.CCUtils;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarConstraintView;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarDayTextView;
import com.overflow.overlab.checkcalendar.R;

import java.io.File;
import java.util.Calendar;

/**
 * Created by over on 3/2/2017.
 * 체크표시
 */

public class SetCheckViewAsyncTask extends AsyncTask<Void, Void, CalendarDayTextView[][]> {

    private CalendarConstraintView calendarConstraintView;

    private CCUtils ccUtils;

    public SetCheckViewAsyncTask(CalendarConstraintView calendarConstraintView) {
        super();
        ccUtils = new CCUtils(calendarConstraintView.getContext());
        this.calendarConstraintView = calendarConstraintView;
    }

    @Override
    protected CalendarDayTextView[][] doInBackground(Void... params) {

        Calendar calendar = calendarConstraintView.positionCalendar;

        File checkListFile = ccUtils.checkListFile(calendar);

        return calendarConstraintView.calendarDayTextViews;
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
