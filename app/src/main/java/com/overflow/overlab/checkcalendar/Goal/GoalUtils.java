package com.overflow.overlab.checkcalendar.Goal;

import android.content.Context;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.overflow.overlab.checkcalendar.CheckCalendarApplication;
import com.overflow.overlab.checkcalendar.Model.GoalCalendarsModel;
import com.overflow.overlab.checkcalendar.Model.GoalDescriptionModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by over on 9/17/2016.
 */
public class GoalUtils {

    private CheckCalendarApplication applicationClass;

    static final String CALENDAR_NAME = "CheckCalendar";
    static final String EMPTY = "EMPTY";
    static final String ERROR_STRING = "ERROR_";

    public GoalUtils(Context context) {
        applicationClass = (CheckCalendarApplication) context.getApplicationContext();
    }

    public String saveGoalListGsonStringFile(String gsonListString) {

        FileOutputStream fos = applicationClass.fileOutputStream(
                applicationClass.goalListFile()
        );
        try {
            fos.write(gsonListString.getBytes());
            fos.close();
            return "Confirm";
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error savegoallist", e.toString());
            return "Error";
        }
    }

    public String getGoalListGsonStringFile() {

        FileInputStream fis = applicationClass.fileInputStream(
                applicationClass.goalListFile()
        );
        try {
            String gsonResult;
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            gsonResult = new String(buffer);
            fis.close();
            return gsonResult;
        } catch (Exception e) {
            Log.d("Error loadgoallist", e.toString());
            return "Error";
        }
    }

    public GoalCalendarsModel addGoalDescriptionModel(
            GoalCalendarsModel goalCalendarsModel,
            GoalDescriptionModel goalDescriptionModel) {

        List<GoalDescriptionModel> mGoalDescriptionModel = goalCalendarsModel.getDescription();

        mGoalDescriptionModel.add(goalDescriptionModel);

        goalCalendarsModel.setSummary(CALENDAR_NAME);
        goalCalendarsModel.setDescription(mGoalDescriptionModel);

        return goalCalendarsModel;
    }

    public GoalDescriptionModel setGoalDescriptionModel(
            String summary, String description,
            DateTime startDate, DateTime endDate) {

        GoalDescriptionModel goalDescriptionModel = new GoalDescriptionModel();

        goalDescriptionModel.setSummary(summary);
        goalDescriptionModel.setDescription(description);
        goalDescriptionModel.setStartDate(startDate);
        goalDescriptionModel.setEndDate(endDate);

        return goalDescriptionModel;
    }

    /**
     * is file exist?
     */
    public static boolean isFile(File file) {
        return file.exists();
    }
}
