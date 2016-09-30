package com.overflow.overlab.checkcalendar.Goal;

import android.content.Context;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CheckCalendarApplication;
import com.overflow.overlab.checkcalendar.Model.GoalCalendarsDescriptionModel;
import com.overflow.overlab.checkcalendar.Model.GoalCalendarsModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Created by over on 9/17/2016.
 */
public class GoalUtils {

    static CheckCalendarApplication applicationClass;

    static final String CALENDAR_NAME = "CheckCalendar";
    static final String EMPTY = "EMPTY";
    static final String ERROR_STRING = "ERROR_";
    static final String CONFIRM = "CONFIRM";

    public GoalUtils(Context context) {
        applicationClass = (CheckCalendarApplication) context.getApplicationContext();
    }

    /**
     * save goal list Gsonformat file
     * @param gsonListString Gson format String
     * @return Success - 'Confirm', Fail - 'Error'
     */
    static public String saveGoalListGsonStringFile(String gsonListString) {

        FileOutputStream fos = applicationClass.fileOutputStream(
                applicationClass.goalListFile()
        );
        try {
            fos.write(gsonListString.getBytes());
            fos.close();
            return CONFIRM;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error savegoallist", e.toString());
            return ERROR_STRING;
        }
    }

    static public String saveGoalCalendarsModel(GoalCalendarsModel goalCalendarsModel) {

        FileOutputStream fos = applicationClass.fileOutputStream(
                applicationClass.goalListFile()
        );
        try {
            fos.write(convertGoalCalendarsModelToString(goalCalendarsModel).getBytes());
            fos.close();
            return CONFIRM;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error savegoallist", e.toString());
            return ERROR_STRING;
        }
    }

    static public String convertGoalCalendarsModelToString(GoalCalendarsModel goalCalendarsModel) {
        return new Gson().toJson(goalCalendarsModel);
    }
    /**
     * get Goal List File to GsonFormat String
     * from Storage
     * @return
     */
    static public String getGoalListGsonStringfromFile() {

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

    /**
     *
     */
    static public GoalCalendarsModel getGoalListGCMfromFile () {
        FileInputStream fis = applicationClass.fileInputStream(
                applicationClass.goalListFile()
        );
        try {
            GoalCalendarsModel goalCalendarsModel;
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            goalCalendarsModel = new Gson().fromJson(new String(buffer), GoalCalendarsModel.class);
            fis.close();
            return goalCalendarsModel;
        } catch (Exception e) {
            Log.d("Error loadgoallist", e.toString());
            return null;
        }
    }

    /**
     * Add GoalCalendarsDescriptionModel into GoalCalendarsModel
     * @param goalCalendarsModel 목표 캘린더 모델
     * @param goalCalendarsDescriptionModel 목표 설명 모델
     * @return GoalCalendarsModel 목표 캘린더 모델
     */
    static public GoalCalendarsModel addGDMtoGCM(
            GoalCalendarsModel goalCalendarsModel, GoalCalendarsDescriptionModel goalCalendarsDescriptionModel) {

        List<GoalCalendarsDescriptionModel> mGoalCalendarsDescriptionModel = goalCalendarsModel.getDescription();

        for(int i = 0; i < mGoalCalendarsDescriptionModel.size(); i++) { //If same summary exist, return null
            if(mGoalCalendarsDescriptionModel.get(i).getSummary() == goalCalendarsDescriptionModel.getSummary()) {
                return null;
            }
        }

        mGoalCalendarsDescriptionModel.add(goalCalendarsDescriptionModel);
        goalCalendarsModel.setDescription(mGoalCalendarsDescriptionModel);
        goalCalendarsModel = removeEmptyGoal(goalCalendarsModel);

        return goalCalendarsModel;
    }

    static public String addGoal(GoalCalendarsDescriptionModel goalCalendarsDescriptionModel) {

        if (!Objects.equals(goalCalendarsDescriptionModel.getSummary(), "")
                && goalCalendarsDescriptionModel.getSummary().trim().length() > 0) {
            GoalCalendarsModel goalCalendarsModel = GoalUtils.getGoalListGCMfromFile();
            goalCalendarsModel =
                    GoalUtils.addGDMtoGCM(goalCalendarsModel, goalCalendarsDescriptionModel);
            GoalUtils.saveGoalCalendarsModel(goalCalendarsModel);
            return CONFIRM;
        } else if (Objects.equals(goalCalendarsDescriptionModel.getSummary(), "")) {
            return ERROR_STRING+"NOT_SUMMARY";
        } else if (goalCalendarsDescriptionModel.getSummary().trim().length() <= 0) {
            return ERROR_STRING+"WHITESPACE";
        }
        return ERROR_STRING;
    }

    /**
     * Set summary, description, startDate, endDate into GoalCalendarsDescriptionModel
     * @param summary 목표 제목
     * @param description 목표 내용
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return GoalCalendarsDescriptionModel
     */
    static public GoalCalendarsDescriptionModel setGoalDescriptionModel (
            String summary, String description,
            DateTime startDate, DateTime endDate) {

        GoalCalendarsDescriptionModel goalCalendarsDescriptionModel = new GoalCalendarsDescriptionModel();

        goalCalendarsDescriptionModel.setSummary(summary);
        goalCalendarsDescriptionModel.setDescription(description);
        goalCalendarsDescriptionModel.setStartDate(startDate);
        goalCalendarsDescriptionModel.setEndDate(endDate);

        return goalCalendarsDescriptionModel;
    }

    /**
     * Update GoalSelectedGoal
     * @param goalCalendarsModel GoalCalendarsModel
     * @param inputGDM GoalCalendarsDescriptionModel
     * @return Updated GoalCalendarsModel
     */
    static public GoalCalendarsModel updateSelectedGoal (
            GoalCalendarsModel goalCalendarsModel, GoalCalendarsDescriptionModel inputGDM) {

        List<GoalCalendarsDescriptionModel> goalCalendarsDescriptionModelList = goalCalendarsModel.getDescription();

        for(int i = 0; i < goalCalendarsDescriptionModelList.size(); i++) {

            GoalCalendarsDescriptionModel mGoalCalendarsDescriptionModel = goalCalendarsDescriptionModelList.get(i);

            if(inputGDM.getSummary() == mGoalCalendarsDescriptionModel.getSummary()) {
                goalCalendarsDescriptionModelList.remove(i);
                goalCalendarsDescriptionModelList.add(i, inputGDM);
                goalCalendarsModel.setDescription(goalCalendarsDescriptionModelList);
                return goalCalendarsModel;
            }
        }
        return null;
    }

    /**
     * Update GoalCalendarsDescriptionModel
     * @param goalCalendarsDescriptionModelList 원본 모델 리스트
     * @param summary 목표 제목
     * @param description 목표 내용
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return goalDescriptionModel 수정된 모델
     */
    static public List<GoalCalendarsDescriptionModel> updateGoalDescriptionModel (
            List<GoalCalendarsDescriptionModel> goalCalendarsDescriptionModelList,
            String summary, String description, DateTime startDate, DateTime endDate, String colorId) {

        for(int i = 0; i < goalCalendarsDescriptionModelList.size(); i++) {
            GoalCalendarsDescriptionModel mGoalCalendarsDescriptionModel = goalCalendarsDescriptionModelList.get(i);
            if(goalCalendarsDescriptionModelList.get(i).getSummary() == summary) {
                mGoalCalendarsDescriptionModel.setSummary(summary);
                mGoalCalendarsDescriptionModel.setDescription(description);
                mGoalCalendarsDescriptionModel.setStartDate(startDate);
                mGoalCalendarsDescriptionModel.setEndDate(endDate);
                mGoalCalendarsDescriptionModel.setColorId(colorId);

                goalCalendarsDescriptionModelList.set(i, mGoalCalendarsDescriptionModel);
                return goalCalendarsDescriptionModelList;
            }
        }
        return null;
    }

    /**
     * Remove Selected Goal
     * @param goalCalendarsModel
     * @param goalSummary
     * @return removed Goal - GoalCalendarsModel
     */
    static public GoalCalendarsModel removeSelectedGoal
            (GoalCalendarsModel goalCalendarsModel, String goalSummary) {

        List<GoalCalendarsDescriptionModel> goalCalendarsDescriptionModelList = goalCalendarsModel.getDescription();

        for(int i = 0; i < goalCalendarsDescriptionModelList.size(); i++) {
            if(goalCalendarsDescriptionModelList.get(i).getSummary() == goalSummary) {
                goalCalendarsDescriptionModelList.remove(i);
                goalCalendarsModel.setDescription(goalCalendarsDescriptionModelList);
                return goalCalendarsModel;
            }
        }
        return null;
    }

    /**
     * Remove EMPTY Goal Description
     * @param
     * @return goalCalendarsModel
     */
    static public GoalCalendarsModel removeEmptyGoal(GoalCalendarsModel goalCalendarsModel) {
        for(int i = 0; i < goalCalendarsModel.getDescription().size(); i++) {
            if (goalCalendarsModel.getDescription().get(i).getSummary() == null
                    || goalCalendarsModel.getDescription().get(i).getSummary().isEmpty()
                    || goalCalendarsModel.getDescription().get(i).getSummary().trim().length() <= 0) {
                goalCalendarsModel.getDescription().remove(i);
            }
        }
        return goalCalendarsModel;
    }

    /**
     * Convert String Time to Calendar
     * @param calendar
     * @return 00 : 00
     */
    static public String convertCalendarStringTime(Calendar calendar) {
        return String.format("%1$tH : %1$tM", calendar);
    }

    static public DateTime convertDateTimeToCalendar(Calendar calendar) {
        return new DateTime(calendar.getTimeInMillis());
    }

    /**
     * is file exist?
     */
    public static boolean isFile(File file) {
        return file.exists();
    }
}
