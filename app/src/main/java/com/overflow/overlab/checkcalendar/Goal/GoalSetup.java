package com.overflow.overlab.checkcalendar.Goal;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CheckCalendarApplication;
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

    private CheckCalendarApplication applicationClass;
    private File goalDataFile;

    static final String CALENDAR_NAME = "CheckCalendar";
    static final String EMPTY = "EMPTY";
    static final String ERROR_STRING = "ERROR_";

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
    public List<String> initGoalSetup() {

        List<String> result = new ArrayList<>();
        File goalListFile = applicationClass.goalListFile();

        try {

            if (GoalUtils.isFile(goalListFile)) { //goal list file exist

                GoalCalendarsModel goalCalendarsModel;

                goalCalendarsModel = new GoalUtils(applicationClass.getApplicationContext())
                        .getGoalListGCMfromFile();

                if(new Gson().toJson(goalCalendarsModel).isEmpty()) { //Goal List Empty
                    Log.d("goal", "list empty, create new file");
                    FileOutputStream fos = applicationClass.fileOutputStream(goalListFile);
                    String gson = initGoalCalendar();
                    Log.d("goal newgson", gson);
                    fos.write(gson.getBytes());
                    fos.close();
                }

                Log.d("goal loadgson", new Gson().toJson(goalCalendarsModel));

                for (int i = 0; i < goalCalendarsModel.getDescription().size(); i++) {
                    if (! EMPTY.equals(goalCalendarsModel.getDescription().get(i))) {
                        result.add(goalCalendarsModel.getDescription().get(i).getSummary());
                    } else {
                        result.add(EMPTY);
                        return result;
                    }
                }

                return result;

            } else { //goal list file not exist
                Log.d("goal", "new file");
                goalListFile.createNewFile();
                FileOutputStream fos = applicationClass.fileOutputStream(goalListFile);
                String gson = initGoalCalendar();
                Log.d("goal newgson", gson);
                fos.write(gson.getBytes());
                result.add(0, EMPTY);
                fos.close();
                return result;
            }

        } catch (Exception e) {
            Log.d("Error initgoal", e.toString());
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
    public String initGoalCalendar() {

        String gsonCalendarsModel;

        GoalCalendarsModel goalCalendarsModel = new GoalCalendarsModel();
        List<GoalCalendarsDescriptionModel> gdmList = new ArrayList<>();
        GoalCalendarsDescriptionModel goalCalendarsDescriptionModel = new GoalCalendarsDescriptionModel();
        goalCalendarsDescriptionModel.setSummary(EMPTY);
        gdmList.add(goalCalendarsDescriptionModel);

        goalCalendarsModel.setSummary(CALENDAR_NAME);
        goalCalendarsModel.setDescription(gdmList);

        gsonCalendarsModel = new Gson().toJson(goalCalendarsModel);

        return gsonCalendarsModel;
    }

    public void initGoalFabSheetMenu() {

    }

}
