package com.overflow.overlab.checkcalendar.Goal;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CCUtils;
import com.overflow.overlab.checkcalendar.Model.GoalCalendarsDescriptionModel;
import com.overflow.overlab.checkcalendar.Model.GoalCalendarsModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by over on 9/1/2016.
 * 목표 셋업 클래스
 * - 목표 설정 캘린더를 생성 CheckCalendar
 * -
 */
public class GoalSetup {

    private CCUtils ccUtils;

    public static final String CALENDAR_NAME = "CheckCalendar";
    public static final String EMPTY = "EMPTY";
    public static final String NULL = "NULL";
    public static final String ERROR_STRING = "ERROR_";

    private Context context;

    public GoalSetup(Context context) {
        this.context = context;
        ccUtils = new CCUtils(context);
    }

    /**
     * 목표설정 초기화 메서드
     * 1. 저장된 목표설정 데이터 확인
     * 3. 데이터의 리스트를 SharedPreferences와 동기화
     * 4. 리스트를 Sheet에 붙이기
     * 구글캘린더와 동기화
     *
     */
    public List<String> initGoalSetup() {

        List<String> result = new ArrayList<>();
        File goalListFile = ccUtils.goalListFile();
        GoalCalendarsModel goalCalendarsModel;

        goalCalendarsModel = new GoalUtils(context).getGoalListGCMfromFile();

        try {
            if (GoalUtils.isFile(goalListFile)) { //goal list file exist

                if(new Gson().toJson(goalCalendarsModel).isEmpty()) { //Goal List Empty
                    initGoalCalendar(goalListFile);
                }
                Log.d("goal loadgson", new Gson().toJson(goalCalendarsModel));
                for (int i = 0; i < goalCalendarsModel.getDescription().size(); i++) {
                    result.add(goalCalendarsModel.getDescription().get(i).getId());
                    ccUtils.setGoalList(
                            goalCalendarsModel.getDescription().get(i).getId(),
                            goalCalendarsModel.getDescription().get(i).getSummary()
                    );
                }
                if(ccUtils.getCurrentGoal()[0].equals(ccUtils.NULL)) {
                    ccUtils.setCurrentGoal(
                            goalCalendarsModel.getDescription().get(0).getId(),
                            goalCalendarsModel.getDescription().get(0).getSummary()
                    );
                }
                return result;

            } else { //goal list file not exist
                Log.d("goal", "create new file");
                goalListFile.createNewFile();
                initGoalCalendar(goalListFile);
                return result;
            }

        } catch (Exception e) {
            Log.d("Error initgoal", e.toString());

            if (goalCalendarsModel == null) { //GoalCalendarsModel is null
                initGoalCalendar(goalListFile);
                return result;
            }
            result.add(0, ERROR_STRING+"INIT");
            return result;
        }

    }

    /**
     * Intialize Goal Calendar
     * Calendar Name - 'Check Calencar'
     * Description - Gson Format
     * @return Gson Format String
     */
    public String setEmptyGoalCalendar() {

        String gsonCalendarsModel;

        GoalCalendarsModel goalCalendarsModel = new GoalCalendarsModel();
        List<GoalCalendarsDescriptionModel> gdmList = new ArrayList<>();
        GoalCalendarsDescriptionModel goalCalendarsDescriptionModel = new GoalCalendarsDescriptionModel();
//        goalCalendarsDescriptionModel.setSummary(EMPTY);
        gdmList.add(goalCalendarsDescriptionModel);

        goalCalendarsModel.setSummary(CALENDAR_NAME);
        goalCalendarsModel.setDescription(gdmList);

        gsonCalendarsModel = new Gson().toJson(goalCalendarsModel);

        return gsonCalendarsModel;
    }

    public String initGoalCalendar(File goalListFile) {

        try {
            Log.d("goal", "create new goal calendar");
            FileOutputStream fos = ccUtils.fileOutputStream(goalListFile);
            String gson = setEmptyGoalCalendar();
            Log.d("goal new gson", gson);
            fos.write(gson.getBytes());
            fos.close();
            return EMPTY;
        } catch (Exception e) {
            Log.d("Error initgoal", e.toString());
            return ERROR_STRING+"FAIL_NEW_GOALCALENDAR";
        }

    }

}
