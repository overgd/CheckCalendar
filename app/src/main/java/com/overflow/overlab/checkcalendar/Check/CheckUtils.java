package com.overflow.overlab.checkcalendar.Check;

import android.content.Context;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CheckCalendarApplication;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsItemsModel;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsTimeModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by over on 10/18/2016.
 */

public class CheckUtils {

    static final String CONFIRMED = "confirmed";
    static final String TENTATIVE = "tentative";
    static final String CANCELLED = "cancelled";

    static final String ERROR_STRING = "ERROR_";
    static final String CONFIRM = "CONFIRM";

    static CheckCalendarApplication applicationClass;

    Context context;

    public CheckUtils(Context context) {
        super();
        this.context = context;
        applicationClass = (CheckCalendarApplication) context.getApplicationContext();
    }

    static public CalendarEventsItemsModel setCheckCalendarEventsItemsModel
            (String status, DateTime[] dateTimes, String memo) {

        CalendarEventsItemsModel calendarEventsItemsModel = new CalendarEventsItemsModel();

        calendarEventsItemsModel.setSummary(applicationClass.getCurrentGoal()[0]);
        calendarEventsItemsModel.setDescription(memo);
        calendarEventsItemsModel.setStatus(status);

        CalendarEventsTimeModel startTime = new CalendarEventsTimeModel();
        startTime.setDateTime(dateTimes[0]);
        CalendarEventsTimeModel endTime = new CalendarEventsTimeModel();
        endTime.setDateTime(dateTimes[1]);

        calendarEventsItemsModel.setStart(startTime);
        calendarEventsItemsModel.setEnd(endTime);

        return calendarEventsItemsModel;
    }

    static public String convertEventsModelToGson (CalendarEventsItemsModel calendarEventsItemsModel) {
        return new Gson().toJson(calendarEventsItemsModel);
    }

    static public String saveCalendarEventsItemsModel (CalendarEventsItemsModel calendarEventsItemsModel) {

        String gson = convertEventsModelToGson(calendarEventsItemsModel);
        DateTime date = calendarEventsItemsModel.getStart().getDateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getValue());
        FileOutputStream fos = applicationClass.fileOutputStream(
                applicationClass.checkListFile(calendar)
        );
        try {
            fos.write(gson.getBytes());
            fos.close();
            return CONFIRM;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error savechecklist", e.toString());
            return ERROR_STRING;
        }
    }

}
