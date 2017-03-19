package com.overflow.overlab.checkcalendar.Check;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CCUtils;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsItemsModel;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsModel;

import java.io.File;
import java.util.Calendar;

/**
 * Created by over on 11/7/2016.
 * 체크를 했을 때 데이터를 저장
 */

public class AddCheckEventAsyncTask extends AsyncTask<Integer, Void, Void> {

    private CCUtils ccUtils;
    private CheckUtils checkUtils;
    private Calendar calendar;

    public AddCheckEventAsyncTask(Context context, Calendar calendar) {
        super();
        this.calendar = calendar;
        ccUtils = new CCUtils(context);
        checkUtils = new CheckUtils(context);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        File checkListFile = ccUtils.checkListFile(calendar);

        DateTime[] dateTimes = new DateTime[2];
        dateTimes[0] = new DateTime(calendar.getTimeInMillis());
        dateTimes[1] = new DateTime(calendar.getTimeInMillis());

        CalendarEventsItemsModel calendarEventsItemsModel =
        checkUtils.setCheckCalendarEventsItemsModel(checkUtils.CONFIRMED, dateTimes, "MEMO");


        CalendarEventsModel checkCalendarEventsModel =
                checkUtils.loadCheckCalendarEventsModel(checkListFile);

        checkUtils.saveCheckEvent(checkCalendarEventsModel, calendarEventsItemsModel);

        Log.d("checklistfile", new Gson().toJson(checkCalendarEventsModel));

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
