package com.overflow.overlab.checkcalendar.Check;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.overflow.overlab.checkcalendar.CCUtils;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarConstraintView;
import com.overflow.overlab.checkcalendar.CalendarView.CalendarDayTextView;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsModel;
import com.overflow.overlab.checkcalendar.R;

import java.io.File;
import java.util.Calendar;

/**
 * Created by over on 3/2/2017.
 * 체크표시
 */

public class AddCheckViewAsyncTask extends AsyncTask<Void, Void, CalendarDayTextView[][]> {

    private CalendarConstraintView calendarConstraintView;
    private CCUtils ccUtils;
    private CheckUtils checkUtils;
    private CalendarEventsModel calendarEventsModel;
    private int[] event_date;

    public AddCheckViewAsyncTask(CalendarConstraintView calendarConstraintView) {
        super();
        ccUtils = new CCUtils(calendarConstraintView.getContext());
        checkUtils = new CheckUtils(calendarConstraintView.getContext());
        this.calendarConstraintView = calendarConstraintView;
    }

    @Override
    protected CalendarDayTextView[][] doInBackground(Void... params) {

        Calendar calendar = calendarConstraintView.positionCalendar;
        File checkListFile = ccUtils.checkListFile(calendar);
        calendarEventsModel = checkUtils.loadCheckCalendarEventsModel(checkListFile);

        if(calendarEventsModel == null) {
            cancel(true);
        }

        event_date = new int[calendarEventsModel.getItems().size()];

        for(int i = 0; i < event_date.length; i++) {
            Calendar calendar_event = Calendar.getInstance();
            calendar_event.setTimeInMillis(calendarEventsModel.getItems().get(i).getStart().getDateTime().getValue());
            event_date[i] = calendar_event.get(Calendar.DATE);
        }

        return calendarConstraintView.calendarDayTextViews;
    }

    @Override
    protected void onPostExecute(CalendarDayTextView[][] calendarDayTextViews) {
        super.onPostExecute(calendarDayTextViews);

        int[] posMain = new int[2];
        int[] posText = new int[2];

        calendarConstraintView.getLocationInWindow(posMain);

        for(int ROW = 0; ROW < 6; ROW++) {
            for(int COL = 0; COL < 7; COL++) {
                calendarDayTextViews[ROW][COL].getLocationInWindow(posText);
                if(posText[0]-posMain[0] == 0) {
                    break;
                }
                if(calendarDayTextViews[ROW][COL].DATE != 0) {
                    for(int k = 0; k < event_date.length; k++) {
                        if(calendarDayTextViews[ROW][COL].DATE == event_date[k]) {
                            if(calendarConstraintView.checkImageView[ROW][COL] == null) {
                                CheckImageView checkImageView =
                                        new CheckImageView(calendarConstraintView.getContext());
                                checkImageView.setImageDrawable(calendarConstraintView.getContext().getResources().getDrawable(R.drawable.ic_check_black_24dp));
                                checkImageView.setX(posText[0] - posMain[0]);
                                checkImageView.setY(posText[1] - posMain[1]);
                                checkImageView.setVisibility(View.VISIBLE);
                                calendarConstraintView.checkImageView[ROW][COL] = checkImageView;
                                calendarConstraintView.addView(calendarConstraintView.checkImageView[ROW][COL]);
                                Log.d("checkimageview set", "add");
                            } else if(calendarConstraintView.checkImageView[ROW][COL] != null){
                                calendarConstraintView.checkImageView[ROW][COL].setVisibility(View.VISIBLE);
                                Log.d("checkimageview set", "exist");
                            }

                        }
                    }
                }
            }
        }
    }
}
