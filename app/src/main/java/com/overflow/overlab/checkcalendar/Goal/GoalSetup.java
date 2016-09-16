package com.overflow.overlab.checkcalendar.Goal;

import android.content.Context;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CheckCalendarApplication;
import com.overflow.overlab.checkcalendar.Model.CalendarsModel;
import com.overflow.overlab.checkcalendar.Model.GoalDescriptionModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by over on 9/1/2016.
 * 목표 셋업 클래스
 * - 목표 설정 캘린더를 생성 CheckCalendar
 * -
 */
public class GoalSetup {

    private CheckCalendarApplication applicationClass;
    private File goalDataFile;

    public GoalSetup(Context context) {
        applicationClass = (CheckCalendarApplication) context.getApplicationContext();
    }

    /**
     * 목표설정 초기화 메서드
     * 1. 저장된 목표설정 데이터 확인
     * 2. 데이터가 있으면 불러오고, 없으면 데이터를 만들고 제목을 'Check Calendar'로 띄우기
     * 3. 데이터의 리스트를 SharedPreferences와 동기화
     * 4. 리스트를 Sheet에 붙이기
     * 구글캘린더와 동기화
     *
     */
    public String initGoalSetup() {

        String result = "NULL";
        File goalListFile = applicationClass.goalListFile();

        try {

            if (isFile(goalListFile)) { //goal list file exist
                FileInputStream fis = applicationClass.fileInputStream(goalListFile);
                byte[] buffer = new byte[fis.available()];
                String loadGoalList;
                fis.read(buffer);
                loadGoalList = new String(buffer);

                if(loadGoalList.isEmpty()) { //list is empty
                    result = "체크캘린더";
                    return result;
                } else { //list is available

                }

            } else { //goal list file not exist
                goalListFile.createNewFile();
                FileOutputStream fos = applicationClass.fileOutputStream(goalListFile);
                fos.write(Byte.parseByte(""));
                fos.close();
                result = "Check Calendar";
                return result;
            }

        } catch (Exception e) {
            Log.d("Error", e.toString());
            result = "ERROR_LOADED_LIST";
            return result;
        }

        result = "Fail Load Goal List";
        return result;
    }

    public String saveGoalListGsonFile(String gsonListString) {

        FileOutputStream fos = applicationClass.fileOutputStream(
                applicationClass.goalListFile()
        );
        try {
            fos.write(Byte.parseByte(gsonListString));
            fos.close();
            return "Confirm";
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error savegoallist", e.toString());
            return "Error";
        }

    }

    public String loadGoalListGsonFile() {

        FileInputStream fis = applicationClass.fileInputStream(
                applicationClass.goalListFile()
        );

        try {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            return new String(buffer);

        } catch (Exception e) {
            Log.d("Error loadgoallist", e.toString());
            return "Error";
        }

    }

    /**
     * Add 'Check Calendar'
     * @return (Gson) CalendarsModel
     */
    public String addCheckCalendar() {

        CalendarsModel goalCalendarsModel = new CalendarsModel();
        GoalDescriptionModel goalDescriptionModel = new GoalDescriptionModel();

        goalCalendarsModel.setSummary("Check Calendar");
        goalCalendarsModel.setDescription(
                new Gson().toJson(goalDescriptionModel)
        );

        return new Gson().toJson(goalCalendarsModel);

    }

    public void addGoal(GoalDescriptionModel goalDescriptionModel) {

        CalendarsModel goalCalendarsModel = new CalendarsModel();
        GoalDescriptionModel mGoalDescriptionModel = goalDescriptionModel;

        goalCalendarsModel.setDescription(
                new Gson().toJson(mGoalDescriptionModel)
        );

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
    public boolean isFile(File file) {
        return file.exists();
    }
}
