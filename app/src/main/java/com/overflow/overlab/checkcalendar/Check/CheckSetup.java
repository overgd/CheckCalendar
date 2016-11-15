package com.overflow.overlab.checkcalendar.Check;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CCUtils;
import com.overflow.overlab.checkcalendar.Goal.GoalSetup;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsItemsModel;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.overflow.overlab.checkcalendar.Goal.GoalSetup.EMPTY;

/**
 * Created by over on 11/15/2016.
 */

public class CheckSetup {

    private CCUtils ccUtils;

    static final String ERROR_STRING = "ERROR_";

    public CheckSetup(Context context) {
        ccUtils = new CCUtils(context);
    }

    public void initCheck() {
        String gsonCheckListModel;
    }

    public String initCheckListFile(File checkListFile) {

        try {
            Log.d("checklist", "create new check list");
            FileOutputStream fos = ccUtils.fileOutputStream(checkListFile);
            String gson = setEmptyChcekCalendar();
            Log.d("check new gson", gson);
            fos.write(gson.getBytes());
            fos.close();
            return EMPTY;
        } catch (Exception e) {
            Log.d("Error initchecklist", e.toString());
            return ERROR_STRING+"FAIL_NEW_CHECKLISTFILE";
        }

    }

    public String setEmptyChcekCalendar() {

        String gsonCheckListModel;

        CalendarEventsItemsModel calendarEventsItemsModel = new CalendarEventsItemsModel();
        List<CalendarEventsItemsModel> calendarEventsItemsModels = new ArrayList<CalendarEventsItemsModel>();
        calendarEventsItemsModels.add(calendarEventsItemsModel);

        CalendarEventsModel calendarEventsModel = new CalendarEventsModel();
        calendarEventsModel.setSummary(GoalSetup.CALENDAR_NAME);
        calendarEventsModel.setItems(calendarEventsItemsModels);

        gsonCheckListModel = new Gson().toJson(calendarEventsModel);

        return gsonCheckListModel;
    }
}
