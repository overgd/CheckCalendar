package com.overflow.overlab.checkcalendar.Check;

import android.content.Context;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CCUtils;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsItemsModel;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsTimeModel;

import java.io.File;
import java.io.FileInputStream;
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

    private Context context;
    private CCUtils ccUtils;

    public CheckUtils(Context context) {
        super();
        this.context = context;
        ccUtils = new CCUtils(context);
    }

    public String loadCheckCalendarEventsItemsModel (File file) {

        try {
            FileInputStream fis = ccUtils.fileInputStream(file);
            CalendarEventsItemsModel checkCalendarEventsItemsModel;
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            checkCalendarEventsItemsModel =
                    new Gson().fromJson(new String(buffer), CalendarEventsItemsModel.class);
            fis.close();
            return new Gson().toJson(checkCalendarEventsItemsModel);
        } catch (NullPointerException ne) {
            Log.d("Error checklistfile", ne.toString());
            return null;
        } catch (IOException ie) {
            Log.d("Error checklistfile", ie.toString());
            return null;
        }

    }

    public CalendarEventsItemsModel setCheckCalendarEventsItemsModel
            (String status, DateTime[] dateTimes, String memo) {

        CalendarEventsItemsModel calendarEventsItemsModel = new CalendarEventsItemsModel();

        calendarEventsItemsModel.setSummary(ccUtils.getCurrentGoal()[0]);
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

    public String saveCalendarEventsItemsModel (CalendarEventsItemsModel calendarEventsItemsModel) {

        String gson = convertEventsModelToGson(calendarEventsItemsModel);
        DateTime date = calendarEventsItemsModel.getStart().getDateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getValue());
        FileOutputStream fos = ccUtils.fileOutputStream(
                ccUtils.checkListFile(calendar)
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
