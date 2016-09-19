package com.overflow.overlab.checkcalendar.Goal;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.CheckCalendarApplication;
import com.overflow.overlab.checkcalendar.Model.GoalCalendarsModel;
import com.overflow.overlab.checkcalendar.Model.GoalDescriptionModel;

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

                String loadGoalList;
                GoalCalendarsModel goalCalendarsModel;

//                FileInputStream fis = applicationClass.fileInputStream(goalListFile);
//                byte[] buffer = new byte[fis.available()];
//                fis.read(buffer);
//                loadGoalList = new String(buffer);

                goalCalendarsModel = new GoalUtils(applicationClass.getApplicationContext())
                        .getGoalListGCMfromFile();

                loadGoalList = new GoalUtils(applicationClass.getApplicationContext())
                        .getGoalListGsonStringFile();

                /**
                 * String으로 되어있는 부분 GoalCalendarsModel로 대체하기.
                 *
                 */
                if(new Gson().toJson(goalCalendarsModel).isEmpty()) {
                    Log.d("goal", "list empty, create new file");
                    FileOutputStream fos = applicationClass.fileOutputStream(goalListFile);
                    String gson = initGoalCalendar();
                    Log.d("goal newgson", gson);
                    fos.write(gson.getBytes());
                    fos.close();
                }

                if(loadGoalList.isEmpty()) { //goal list empty
                    Log.d("goal", "new file");
                    FileOutputStream fos = applicationClass.fileOutputStream(goalListFile);
                    String gson = initGoalCalendar();
                    Log.d("goal newgson", gson);
                    fos.write(gson.getBytes());
                    fos.close();
                }

                Log.d("goal loadgson", loadGoalList);
                GoalCalendarsModel calendarsModel =
                        new Gson().fromJson(loadGoalList, GoalCalendarsModel.class);

                if(EMPTY.equals(calendarsModel.getDescription().get(0).getSummary())) {
                    result.add(0, EMPTY);
                } else {
                    result.add(0, calendarsModel.getDescription().get(0).getSummary());
                }

                return result;

            } else { //goal list file not exist
                Log.d("goal", "new file");
                goalListFile.createNewFile();
                FileOutputStream fos = applicationClass.fileOutputStream(goalListFile);
                String gson = initGoalCalendar();
                Log.d("goal newgson", gson);
                fos.write(gson.getBytes());
                fos.close();
                result.add(0, CALENDAR_NAME);
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
        List<GoalDescriptionModel> gdmList = new ArrayList<>();
        GoalDescriptionModel goalDescriptionModel = new GoalDescriptionModel();
        goalDescriptionModel.setSummary(EMPTY);
        gdmList.add(goalDescriptionModel);

        goalCalendarsModel.setSummary(CALENDAR_NAME);
        goalCalendarsModel.setDescription(gdmList);

        gsonCalendarsModel = new Gson().toJson(goalCalendarsModel);

        return gsonCalendarsModel;
    }

}
