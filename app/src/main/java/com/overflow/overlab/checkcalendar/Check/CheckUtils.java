package com.overflow.overlab.checkcalendar.Check;

import android.content.Context;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CCUtils;
import com.overflow.overlab.checkcalendar.Goal.GoalSetup;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsItemsModel;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsModel;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsTimeModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by over on 10/18/2016.
 * 목표체크에 필요한 Utils
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

    /**
    *
    **/
    public CalendarEventsModel loadCheckCalendarEventsModel (File file) {

        try {
            FileInputStream fis = ccUtils.fileInputStream(file);
            CalendarEventsModel calendarEventsModel;
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            calendarEventsModel =
                    new Gson().fromJson(new String(buffer), CalendarEventsModel.class);
            fis.close();
            return calendarEventsModel;

        } catch (NullPointerException ne) {
            Log.d("Error checklistfile", ne.toString());
            return null;
        } catch (IOException ie) {
            Log.d("Error checklistfile", ie.toString());
            return null;
        }

    }

    public CalendarEventsModel setCheckCalendarEventsModel (List<CalendarEventsItemsModel> calendarEventsItemsModels) {

        CalendarEventsModel calendarEventsModel = new CalendarEventsModel();

        calendarEventsModel.setSummary(GoalSetup.CALENDAR_NAME);
        calendarEventsModel.setItems(calendarEventsItemsModels);

        return calendarEventsModel;
    }

    public CalendarEventsItemsModel setCheckCalendarEventsItemsModel
            (String status, DateTime[] dateTimes, String memo) {

        CalendarEventsItemsModel calendarEventsItemsModel = new CalendarEventsItemsModel();

        calendarEventsItemsModel.setSummary(ccUtils.getCurrentGoal()[1]);
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

    public List<CalendarEventsItemsModel> addCheckCalendarEventsItemsModels
            (List<CalendarEventsItemsModel> calendarEventsItemsModels,
             CalendarEventsItemsModel calendarEventsItemsModel) {

        calendarEventsItemsModels.add(calendarEventsItemsModel);

        return calendarEventsItemsModels;
    }

    public String saveCheckCalendarEventsFile
            (CalendarEventsModel calendarEventsModel) {

        DateTime datetime = calendarEventsModel.getItems().get(0).getStart().getDateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime.getValue());

        FileOutputStream fos = ccUtils.fileOutputStream(
                ccUtils.checkListFile(calendar)
        );
        try {
            fos.write(new Gson().toJson(calendarEventsModel).getBytes());
            fos.close();
            return CONFIRM;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error savechecklist", e.toString());
            return ERROR_STRING;
        }

    }

    public String saveCheckEvent
            (CalendarEventsModel calendarEventsModel, CalendarEventsItemsModel calendarEventsItemsModel) {

        List<CalendarEventsItemsModel> calendarEventsItemsModels;

        if(calendarEventsModel.getItems() == null) {
            calendarEventsItemsModels = new ArrayList<CalendarEventsItemsModel>();
        } else {
            calendarEventsItemsModels = calendarEventsModel.getItems();
        }

        calendarEventsItemsModels.add(calendarEventsItemsModel);
        calendarEventsModel.setItems(calendarEventsItemsModels);

        Log.d("checklisttime", calendarEventsItemsModels.get(0).getStart().getDateTime().toString());

        saveCheckCalendarEventsFile(calendarEventsModel);

        return CONFIRM;
    }

}
