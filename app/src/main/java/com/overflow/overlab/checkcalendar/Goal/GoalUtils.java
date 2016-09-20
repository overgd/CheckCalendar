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
     * Add GoalCalendarsDescriptionModel into GoalCalendarsModel
     * @param goalCalendarsModel 목표 캘린더 모델
     * @param goalCalendarsDescriptionModel 목표 설명 모델
     * @return GoalCalendarsModel 목표 캘린더 모델
     */
    public GoalCalendarsModel addGDMintoGCM(
            GoalCalendarsModel goalCalendarsModel, GoalCalendarsDescriptionModel goalCalendarsDescriptionModel) {

        List<GoalCalendarsDescriptionModel> mGoalCalendarsDescriptionModel = goalCalendarsModel.getDescription();

        for(int i = 0; i < mGoalCalendarsDescriptionModel.size(); i++) { //If same summary exist, return null
            if(mGoalCalendarsDescriptionModel.get(i).getSummary() == goalCalendarsDescriptionModel.getSummary()) {
                return null;
            }
            removeEmptyGoalDescription(mGoalCalendarsDescriptionModel, i);
        }

        mGoalCalendarsDescriptionModel.add(goalCalendarsDescriptionModel);

        goalCalendarsModel.setDescription(mGoalCalendarsDescriptionModel);

        return goalCalendarsModel;
    }

    /**
     * Set summary, description, startDate, endDate into GoalCalendarsDescriptionModel
     * @param summary 목표 제목
     * @param description 목표 내용
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return GoalCalendarsDescriptionModel
     */
    public GoalCalendarsDescriptionModel setGoalDescriptionModel (
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
    public GoalCalendarsModel updateSelectedGoal (
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
    public List<GoalCalendarsDescriptionModel> updateGoalDescriptionModel (
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
    public GoalCalendarsModel removeSelectedGoal
            (GoalCalendarsModel goalCalendarsModel, String goalSummary) {

        List<GoalCalendarsDescriptionModel> goalCalendarsDescriptionModelList = goalCalendarsModel.getDescription();

        for(int i = 0; i < goalCalendarsDescriptionModelList.size(); i++) {
            if(goalCalendarsDescriptionModelList.get(i).getSummary() == goalSummary) {
                goalCalendarsDescriptionModelList.remove(i);
                if (goalCalendarsDescriptionModelList.size() == 0) {
                    GoalCalendarsDescriptionModel emptyGDM = new GoalCalendarsDescriptionModel();
                    emptyGDM.setSummary(EMPTY);
                    goalCalendarsDescriptionModelList.add(emptyGDM);
                }
                goalCalendarsModel.setDescription(goalCalendarsDescriptionModelList);
                return goalCalendarsModel;
            }
        }
        return null;
    }

    /**
     * Remove EMPTY Goal Description
     * @param goalCalendarsDescriptionModelList
     * @return goalCalendarsModel
     */
    public List<GoalCalendarsDescriptionModel> removeEmptyGoalDescription(List<GoalCalendarsDescriptionModel> goalCalendarsDescriptionModelList, int index) {
        if (goalCalendarsDescriptionModelList.get(index).getSummary() == EMPTY) {
            goalCalendarsDescriptionModelList.remove(index);
        }
        return goalCalendarsDescriptionModelList;
    }

    /**
     * is file exist?
     */
    public static boolean isFile(File file) {
        return file.exists();
    }
}
