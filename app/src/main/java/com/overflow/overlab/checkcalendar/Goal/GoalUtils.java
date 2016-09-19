package com.overflow.overlab.checkcalendar.Goal;

import android.content.Context;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    /**
     * save goal list Gsonformat file
     * @param gsonListString Gson format String
     * @return Success - 'Confirm', Fail - 'Error'
     */
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

    /**
     * get Goal List File to GsonFormat String
     * from Storage
     * @return
     */
    public String getGoalListGsonStringfromFile() {

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
    public GoalCalendarsModel getGoalListGCMfromFile () {
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
     * Add GoalDescriptionModel into GoalCalendarsModel
     * @param goalCalendarsModel 목표 캘린더 모델
     * @param goalDescriptionModel 목표 설명 모델
     * @return GoalCalendarsModel 목표 캘린더 모델
     */
    public GoalCalendarsModel addGDMintoGCM(
            GoalCalendarsModel goalCalendarsModel, GoalDescriptionModel goalDescriptionModel) {

        List<GoalDescriptionModel> mGoalDescriptionModel = goalCalendarsModel.getDescription();

        for(int i = 0; i < mGoalDescriptionModel.size(); i++) { //If same summary exist, return null
            if(mGoalDescriptionModel.get(i).getSummary() == goalDescriptionModel.getSummary()) {
                return null;
            }
            removeEmptyGoalDescription(mGoalDescriptionModel, i);
        }

        mGoalDescriptionModel.add(goalDescriptionModel);

        goalCalendarsModel.setDescription(mGoalDescriptionModel);

        return goalCalendarsModel;
    }

    /**
     * Set summary, description, startDate, endDate into GoalDescriptionModel
     * @param summary 목표 제목
     * @param description 목표 내용
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return GoalDescriptionModel
     */
    public GoalDescriptionModel setGoalDescriptionModel (
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
     * Update GoalSelectedGoal
     * @param goalCalendarsModel GoalCalendarsModel
     * @param inputGDM GoalDescriptionModel
     * @return Updated GoalCalendarsModel
     */
    public GoalCalendarsModel updateSelectedGoal (
            GoalCalendarsModel goalCalendarsModel, GoalDescriptionModel inputGDM) {

        List<GoalDescriptionModel> goalDescriptionModelList = goalCalendarsModel.getDescription();

        for(int i = 0; i < goalDescriptionModelList.size(); i++) {

            GoalDescriptionModel mGoalDescriptionModel = goalDescriptionModelList.get(i);

            if(inputGDM.getSummary() == mGoalDescriptionModel.getSummary()) {
                goalDescriptionModelList.remove(i);
                goalDescriptionModelList.add(i, inputGDM);
                goalCalendarsModel.setDescription(goalDescriptionModelList);
                return goalCalendarsModel;
            }
        }
        return null;
    }

    /**
     * Update GoalDescriptionModel
     * @param goalDescriptionModelList 원본 모델 리스트
     * @param summary 목표 제목
     * @param description 목표 내용
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return goalDescriptionModel 수정된 모델
     */
    public List<GoalDescriptionModel> updateGoalDescriptionModel (
            List<GoalDescriptionModel> goalDescriptionModelList,
            String summary, String description, DateTime startDate, DateTime endDate) {

        for(int i = 0; i < goalDescriptionModelList.size(); i++) {
            GoalDescriptionModel mGoalDescriptionModel = goalDescriptionModelList.get(i);
            if(goalDescriptionModelList.get(i).getSummary() == summary) {
                mGoalDescriptionModel.setSummary(summary);
                mGoalDescriptionModel.setDescription(description);
                mGoalDescriptionModel.setStartDate(startDate);
                mGoalDescriptionModel.setEndDate(endDate);

                goalDescriptionModelList.set(i, mGoalDescriptionModel);
                return goalDescriptionModelList;
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
    public GoalCalendarsModel removeSelectedGoal
            (GoalCalendarsModel goalCalendarsModel, String goalSummary) {

        List<GoalDescriptionModel> goalDescriptionModelList = goalCalendarsModel.getDescription();

        for(int i = 0; i < goalDescriptionModelList.size(); i++) {
            if(goalDescriptionModelList.get(i).getSummary() == goalSummary) {
                goalDescriptionModelList.remove(i);
                if (goalDescriptionModelList.size() == 0) {
                    GoalDescriptionModel emptyGDM = new GoalDescriptionModel();
                    emptyGDM.setSummary(EMPTY);
                    goalDescriptionModelList.add(emptyGDM);
                }
                goalCalendarsModel.setDescription(goalDescriptionModelList);
                return goalCalendarsModel;
            }
        }
        return null;
    }

    /**
     * Remove EMPTY Goal Description
     * @param goalDescriptionModelList
     * @return goalCalendarsModel
     */
    public List<GoalDescriptionModel> removeEmptyGoalDescription(List<GoalDescriptionModel> goalDescriptionModelList, int index) {
        if (goalDescriptionModelList.get(index).getSummary() == EMPTY) {
            goalDescriptionModelList.remove(index);
        }
        return goalDescriptionModelList;
    }

    /**
     * is file exist?
     */
    public static boolean isFile(File file) {
        return file.exists();
    }
}
