package com.overflow.overlab.checkcalendar.Goal;

import android.content.Context;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CCUtils;
import com.overflow.overlab.checkcalendar.Model.GoalCalendarsDescriptionModel;
import com.overflow.overlab.checkcalendar.Model.GoalCalendarsModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by over on 9/17/2016.
 */
public class GoalUtils {

    private CCUtils ccUtils;

    static final String CALENDAR_NAME = "CheckCalendar";
    static final String EMPTY = "EMPTY";
    static final String ERROR_STRING = "ERROR_";
    static final String CONFIRM = "CONFIRM";

    public GoalUtils(Context context) {
        ccUtils = new CCUtils(context);
    }

    /**
     * save goal list Gsonformat file
     * @param gsonListString Gson format String
     * @return Success - 'Confirm', Fail - 'Error'
     */
    public String saveGoalListGsonStringFile(String gsonListString) {
        FileOutputStream fos = ccUtils.fileOutputStream(
                ccUtils.goalListFile()
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

    public String saveGoalCalendarsModel(GoalCalendarsModel goalCalendarsModel) {
        FileOutputStream fos = ccUtils.fileOutputStream(
                ccUtils.goalListFile()
        );
        try {
            fos.write(convertGoalCalendarsModelToGson(goalCalendarsModel).getBytes());
            fos.close();
            return CONFIRM;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error savegoallist", e.toString());
            return ERROR_STRING;
        }
    }

    public String convertGoalCalendarsModelToGson(GoalCalendarsModel goalCalendarsModel) {
        return new Gson().toJson(goalCalendarsModel);
    }
    /**
     * get Goal List File to GsonFormat String
     * from Storage
     * @return
     */
    public String getGoalListGsonStringfromFile() {

        FileInputStream fis = ccUtils.fileInputStream(
                ccUtils.goalListFile()
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
     * */
    public String getGoalDescription(String goalId) {
        String result = null;
        GoalCalendarsModel goalCalendarsModel = getGoalListGCMfromFile();
        List<GoalCalendarsDescriptionModel> goalCalendarsDescriptionModels = goalCalendarsModel.getDescription();
        for(int i = 0; i < goalCalendarsDescriptionModels.size(); i++) {
            if(Objects.equals(goalId, goalCalendarsDescriptionModels.get(i).getId())) {
                result = goalCalendarsDescriptionModels.get(i).getDescription();
            }
        }
        return result;
    }

    /**
     *
     */
    public GoalCalendarsModel getGoalListGCMfromFile () {
        FileInputStream fis = ccUtils.fileInputStream(
                ccUtils.goalListFile()
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
    static public GoalCalendarsModel addGDMintoGCM(
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

    public String addGoal(GoalCalendarsDescriptionModel goalCalendarsDescriptionModel) {
        if (!Objects.equals(goalCalendarsDescriptionModel.getSummary(), "") &&
                goalCalendarsDescriptionModel.getSummary().trim().length() > 0 &&
                !goalCalendarsDescriptionModel.getSummary().equals(ccUtils.NULL)) {
            GoalCalendarsModel goalCalendarsModel = getGoalListGCMfromFile();
            for(GoalCalendarsDescriptionModel gcd : goalCalendarsModel.getDescription()) {
                if (goalCalendarsDescriptionModel.getSummary().equals(gcd.getSummary())) {
                    return ERROR_STRING+"SAME_SUMMARY";
                }
            }
            goalCalendarsModel =
                    GoalUtils.addGDMintoGCM(goalCalendarsModel, goalCalendarsDescriptionModel);
            saveGoalCalendarsModel(goalCalendarsModel);
            return CONFIRM;
        } else if (Objects.equals(goalCalendarsDescriptionModel.getSummary(), "")) {
            return ERROR_STRING+"NOT_SUMMARY";
        } else if (goalCalendarsDescriptionModel.getSummary().trim().length() <= 0) {
            return ERROR_STRING+"WHITESPACE";
        } else if (goalCalendarsDescriptionModel.getSummary().equals(ccUtils.NULL)) {
            return ERROR_STRING+"DONT_NULL";
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

        goalCalendarsDescriptionModel.setId(UUID.randomUUID().toString());
        goalCalendarsDescriptionModel.setSummary(summary);
        goalCalendarsDescriptionModel.setDescription(description);
        goalCalendarsDescriptionModel.setStartDate(startDate);
        goalCalendarsDescriptionModel.setEndDate(endDate);

        return goalCalendarsDescriptionModel;
    }

    /**
     * Update GoalSelectedGoal
     * @param goalCalendarsModel GoalCalendarsModel
     * @param goalCalendarsDescriptionModel GoalCalendarsDescriptionModel
     * @return Updated GoalCalendarsModel
     */
    static public GoalCalendarsModel updateSelectedGoal (
            GoalCalendarsModel goalCalendarsModel, GoalCalendarsDescriptionModel goalCalendarsDescriptionModel) {

        List<GoalCalendarsDescriptionModel> goalCalendarsDescriptionModelList = goalCalendarsModel.getDescription();

        for(int i = 0; i < goalCalendarsDescriptionModelList.size(); i++) {

            GoalCalendarsDescriptionModel mGoalCalendarsDescriptionModel = goalCalendarsDescriptionModelList.get(i);

            if(goalCalendarsDescriptionModel.getSummary() == mGoalCalendarsDescriptionModel.getSummary()) {
                goalCalendarsDescriptionModelList.remove(i);
                goalCalendarsDescriptionModelList.add(i, goalCalendarsDescriptionModel);
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
     * @param goalId
     * @return removed Goal - GoalCalendarsModel
     */
    static public GoalCalendarsModel removeSelectedGoal
            (GoalCalendarsModel goalCalendarsModel, String goalId) {

        List<GoalCalendarsDescriptionModel> goalCalendarsDescriptionModelList = goalCalendarsModel.getDescription();

        for(int i = 0; i < goalCalendarsDescriptionModelList.size(); i++) {
            if(goalCalendarsDescriptionModelList.get(i).getId().equals(goalId)) {
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
