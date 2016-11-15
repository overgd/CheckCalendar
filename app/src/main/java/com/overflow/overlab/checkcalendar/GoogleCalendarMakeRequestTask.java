/*
package com.overflow.overlab.checkcalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Events;
import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsItemsModel;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsModel;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsTimeModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

*/
/**
 * Created by over on 6/29/2016.
 *//*


class GoogleCalendarMakeRequestTask extends AsyncTask<java.util.Calendar, Void, String> {

    private final static int EVENTS_IN_MONTH_VIEW = 1000;

    int MODE;

    private Context context;

    private CheckCalendarApplication applicationClass;
    private SharedPreferences colorSharePreferences;

    private Exception mLastError = null;
    private java.util.Calendar selectedCalendar;

    private File savedFile;
    private String loadGsonString;

    private List<TextView[][]> eventView; //Month Event Column View

    public GoogleCalendarMakeRequestTask(Context context, List<TextView[][]> eventView) {

        this.context = context;
        applicationClass = (CheckCalendarApplication) context.getApplicationContext();

        colorSharePreferences = applicationClass.getApplicationContext().getSharedPreferences("Colors", Context.MODE_PRIVATE);

        this.eventView = eventView;

        MODE = EVENTS_IN_MONTH_VIEW;

    }


    @Override
    protected String doInBackground(java.util.Calendar... params) {

        selectedCalendar = params[0];

        File savedFile = applicationClass.calendarDataFile(selectedCalendar);
        this.savedFile = savedFile;

        try {

            if (savedFile.exists()) { //저장된 파일이 있으면 실행

                FileInputStream fileInputStream = applicationClass.fileInputStream(savedFile);
                byte[] buffer = new byte[fileInputStream.available()];
                fileInputStream.read(buffer);
                loadGsonString = new String(buffer);

            } else if (!savedFile.exists() || loadGsonString.isEmpty()) { //저장된 파일이 없으면 새로 만들기

                savedFile.createNewFile();
                Log.d("File", "Created Complete : " + savedFile.getName());
                loadGsonString = savedFlieCalendarEventsModel(getEventsInMonth(selectedCalendar));

            }

            return loadGsonString;

        } catch (Exception e) {
            Log.d("Error doinbackground", e.toString());
            mLastError = e;
            cancel(true);
        }

        return null;
    }

    private String syncCalendarEvent(String gsonString) throws Exception {

        java.util.Calendar mCalendar = selectedCalendar;
        String resultString = "NULL";

        CalendarEventsModel savedEventsModel = new Gson().fromJson(gsonString, CalendarEventsModel.class);
        String savedSyncToken = savedEventsModel.getNextSyncToken(); // 저장된 토큰
        Events compareSyncTokenEvents = getEventsSyncToken(savedSyncToken); //토큰으로 비교한 Events 결과

        String currentSyncToken = compareSyncTokenEvents.getNextSyncToken(); // 서버에서 저장된 토큰과 비교한 토큰

        Log.d("savedSyncToken", savedSyncToken);
        Log.d("currentSyncToken", currentSyncToken);

        if (savedSyncToken.isEmpty()) { //저장된 syncToken이 없을 경우

            return resultString = "NO_SYNC_TOKEN";

        } else if (Objects.equals(savedSyncToken, currentSyncToken)) {  //저장된 syncToken과 가져온 Token이 같을 경우

            Log.d("Compare Token", "same");

            return resultString = "SAME_SYNC_TOKEN";

        } else if (!Objects.equals(savedSyncToken, currentSyncToken)) { //저장된 syncToken과 가져온 Token이 다를 경우

            Log.d("Compare Token", "not same");

            //eventID를 비교
            if (compareSyncTokenEvents.getItems().size() != 0) {

                String[] savedEventID = new String[savedEventsModel.getItems().size()];
                String[] compareEventID = new String[compareSyncTokenEvents.getItems().size()];

                for (int i = 0; i < savedEventsModel.getItems().size(); i++) {
                    savedEventID[i] = savedEventsModel.getItems().get(i).getId();
                }
                for (int i = 0; i < compareSyncTokenEvents.getItems().size(); i++) {
                    compareEventID[i] = compareSyncTokenEvents.getItems().get(i).getId();
                }

                for (String savedID : savedEventID) {
                    for (String compareID : compareEventID) {
                        if (Objects.equals(savedID, compareID)) { //저장된 ID와 비교할 ID와 일치하는게 있는지 비교
                            return resultString = "UPDATE_EVENTS";

                        } else {                                    //저장된 ID와 비교할 ID가 일치하지 않으면 추가된게 있는지 비교
                            for (int i = 0; i < compareSyncTokenEvents.getItems().size(); i++) {
                                if (compareSyncTokenEvents.getItems().get(i).getStart() != null) {

                                    DateTime compareDateTime = null;
                                    java.util.Calendar compareCalendar = java.util.Calendar.getInstance();
                                    compareCalendar.clear();

                                    if (compareSyncTokenEvents.getItems().get(i).getStart().getDateTime() != null) {
                                        compareDateTime = compareSyncTokenEvents.getItems().get(i).getStart().getDateTime();
                                    } else if (compareSyncTokenEvents.getItems().get(i).getStart().getDate() != null) {
                                        compareDateTime = compareSyncTokenEvents.getItems().get(i).getStart().getDate();
                                    }

                                    compareCalendar.setTimeInMillis(compareDateTime.getValue());
                                    if (compareCalendar.get(java.util.Calendar.MONTH) == mCalendar.get(java.util.Calendar.MONTH)) {
                                        return resultString = "UPDATE_EVENTS";
                                    }

                                }
                            }
                            return resultString = "NO_UPDATE_EVENTS";
                        }
                    }
                }

            }

        }

        return resultString = "NULL";

    }

    */
/*
    입력된 캘린더 이벤트 모델을 Gson 포맷으로 변경 후 String 형식으로 파일로 저장
     *//*

    private String savedFlieCalendarEventsModel(Events events) throws Exception {

        try {

            CalendarEventsModel eventsMonthModel = new CalendarEventsModel();

            eventsMonthModel.setSummary(events.getSummary());
            eventsMonthModel.setTimeZone(events.getTimeZone());
            eventsMonthModel.setNextSyncToken(events.getNextSyncToken());
//            eventsMonthModel.setColorId();

            List<CalendarEventsItemsModel> calendarItems = new ArrayList<>(events.getItems().size());

            for (int i = 0; i < events.getItems().size(); i++) {

                CalendarEventsItemsModel items = new CalendarEventsItemsModel();
                CalendarEventsTimeModel timeModel = new CalendarEventsTimeModel();

                items.setId(events.getItems().get(i).getId());
                items.setSummary(events.getItems().get(i).getSummary());
                items.setStatus(events.getItems().get(i).getStatus());
                items.setColorId(events.getItems().get(i).getColorId());

                if (events.getItems().get(i).getEnd().getDate() != null
                        && events.getItems().get(i).getStart().getDate() != null) {

                    timeModel.setDate(events.getItems().get(i).getEnd().getDate());
                    timeModel.setTimeZone(events.getItems().get(i).getEnd().getTimeZone());
                    items.setEnd(timeModel);

                    timeModel.setDate(events.getItems().get(i).getStart().getDate());
                    timeModel.setTimeZone(events.getItems().get(i).getStart().getTimeZone());
                    items.setStart(timeModel);

                } else if (events.getItems().get(i).getEnd().getDateTime() != null
                        && events.getItems().get(i).getStart().getDateTime() != null) {

                    timeModel.setDateTime(events.getItems().get(i).getEnd().getDateTime());
                    timeModel.setTimeZone(events.getItems().get(i).getEnd().getTimeZone());
                    items.setEnd(timeModel);

                    timeModel.setDateTime(events.getItems().get(i).getStart().getDateTime());
                    timeModel.setTimeZone(events.getItems().get(i).getStart().getTimeZone());
                    items.setStart(timeModel);

                }

                calendarItems.add(items);
            }

            eventsMonthModel.setItems(calendarItems);

            String eventsString = new Gson().toJson(eventsMonthModel);

            FileOutputStream fileOutputStream = applicationClass.fileOutputStream(savedFile);
            fileOutputStream.write(eventsString.getBytes());
            fileOutputStream.close();

            return eventsString;

            //저장된 파일 확인
//            FileInputStream fileInputStream = applicationClass.fileInputStream(savedFile);
//            byte[] buffer = new byte[fileInputStream.available()];
//            fileInputStream.read(buffer);
//            String resultString = new String(buffer);
//
//            CalendarEventsModel savedModel = new Gson().fromJson(resultString, CalendarEventsModel.class);
//
//            String savedString = new Gson().toJson(savedModel);
//            Log.d("savedfile getstring", savedString);

        } catch (Exception e) {
            Log.d("Error savedfile", e.toString());
            mLastError = e;
            cancel(true);
        }

        return null;

    }

    private Events getEventsSyncToken(String syncToken) throws Exception {

        Events events = applicationClass.mCalendarService.events().list("primary")
                .setSyncToken(syncToken)
                .execute();

        return events;
    }

    private Events getEventsInMonth(java.util.Calendar selectedCalendar) throws Exception {

        java.util.Calendar makeCalendar = java.util.Calendar.getInstance();
        //선택한 달력의 1일로 초기화
        makeCalendar.clear();
        makeCalendar.set(
                selectedCalendar.get(java.util.Calendar.YEAR),
                selectedCalendar.get(java.util.Calendar.MONTH),
                1
        );

        DateTime minDateTime = new DateTime(makeCalendar.getTimeInMillis());
//        Log.d("DateTime min", minDateTime.toString());
        makeCalendar.set(java.util.Calendar.DATE, makeCalendar.getActualMaximum(java.util.Calendar.DATE));
        DateTime maxDateTime = new DateTime(makeCalendar.getTimeInMillis());
//        Log.d("DateTime max", minDateTime.toString());

        Events events = applicationClass.mCalendarService.events().list("primary")
                .setSingleEvents(true)
                .setTimeMin(minDateTime)
                .setTimeMax(maxDateTime)
                .execute();

        return events;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String gsonString) {

        super.onPostExecute(gsonString);

        switch (MODE) {

            case EVENTS_IN_MONTH_VIEW:

                setEventsInMonthView(gsonString);

                AsyncTask<String, Void, Void> syncEventsTask = new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {

                        try {
                            String syncResult = syncCalendarEvent(params[0]);
                            Log.d("SyncResult", syncResult);
                            switch (syncResult) {
                                case "NO_SYNC_TOKEN": // SYNC 토큰이 없으면 Gson데이터 생성
                                    setEventsInMonthView(savedFlieCalendarEventsModel(getEventsInMonth(selectedCalendar)));
                                    break;
                                case "SAME_SYNC_TOKEN": // Sync토큰이 같으면 그대로 출력
                                    break;
                                case "UPDATE_EVENTS":  // Sync토큰이 다르고 변경된 내용이 있으면 Gson데이터 재생성
                                    setEventsInMonthView(savedFlieCalendarEventsModel(getEventsInMonth(selectedCalendar)));
                                    break;
                                case "NO_UPDATE_EVENTS": // Sync토큰이 다르고 변경된 내용이 없으면 그대로 출력
                                    break;
                                case "NULL":
                                    break;
                            }
                            cancel(true);
                        } catch (UserRecoverableAuthIOException uraioe) {
                            Log.d("SyncToken uraioe Error", uraioe.toString());
                            mLastError = uraioe;
                            cancel(true);

                        } catch (Exception e) {
                            String errorCode = e.getMessage().substring(0, 3);
                            if(Objects.equals(errorCode, "410")) {
                                Log.d("SyncToken Error", "410 Error! Require Full Sync");
                                try {
                                    setEventsInMonthView(savedFlieCalendarEventsModel(getEventsInMonth(selectedCalendar)));
                                } catch (Exception e1) {
                                    Log.d("SyncToken Error", e.toString());
                                }
                            }
                            mLastError = e;
                            cancel(true);
                        }

                        cancel(true);
                        return null;
                    }

                    @Override
                    protected void onCancelled() {
                        if (mLastError != null) {
                            if (mLastError instanceof UserRecoverableAuthIOException) {
                                (applicationClass.getMainActivity()).startActivityForResult(
                                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                                        MainActivity.REQUEST_AUTHORIZATION
                                );
                            }
                        }
                    }
                };

                syncEventsTask.execute(gsonString);

                if (syncEventsTask.isCancelled()) {
                    cancel(true);
                }

        }

    }

    */
/*
    월 달력 뷰에 이벤트 세팅
     *//*

    private void setEventsInMonthView(String gsonString) {

        CalendarEventsModel eventsModel = new Gson().fromJson(gsonString, CalendarEventsModel.class);

        java.util.Calendar mCalendar = java.util.Calendar.getInstance();
        mCalendar.clear();
        DateTime dateTime;

        Log.d("gsonString", gsonString);
        if (gsonString.isEmpty()) cancel(true);

        try {

            List<CalendarEventsItemsModel> ev = eventsModel.getItems();

            for (int i = 0; i < ev.size(); i++) { //ev 사이즈는 한달 동안의 이벤트 개수

                //각 이벤트의 시작 시간
                if (ev.get(i).getStart().getDateTime() != null) {
                    dateTime = ev.get(i).getStart().getDateTime();
                } else {
                    dateTime = ev.get(i).getStart().getDate();
                }

                mCalendar.setTimeInMillis(dateTime.getValue());
//                    Log.d("eventDateTime", String.valueOf(mCalendar.getTime()));

                for (int j = 0; j < eventView.size(); j++) {
                    TextView[][] textview;
                    textview = eventView.get(j);

                    if (textview[mCalendar.get(java.util.Calendar.WEEK_OF_MONTH) - 1]
                            [mCalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1]
                            .getText() == "") {

                        textview[mCalendar.get(java.util.Calendar.WEEK_OF_MONTH) - 1]
                                [mCalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1]
                                .setText(ev.get(i).getSummary());

                        if (colorSharePreferences.getString(
                                "Event.Color." + ev.get(i).getColorId(), "null")
                                != "null") {
                            textview[mCalendar.get(java.util.Calendar.WEEK_OF_MONTH) - 1]
                                    [mCalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1]
                                    .setBackgroundColor(Color.parseColor(colorSharePreferences.getString(
                                            "Event.Color." + ev.get(i).getColorId(), "null")));
                        } else {
                            textview[mCalendar.get(java.util.Calendar.WEEK_OF_MONTH) - 1]
                                    [mCalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1]
                                    .setBackgroundColor(Color.parseColor(colorSharePreferences.getString(
                                            "Calendar.Color.16", "null")));
                        }

                        textview[mCalendar.get(java.util.Calendar.WEEK_OF_MONTH) - 1]
                                [mCalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1]
                                .setTextColor(Color.parseColor("#FFFFFF"));

                        break;
                    }
                }

            }

        } catch (Exception e) {
            Log.d("Error setEvents", e.toString());
            mLastError = e;
            cancel(true);
        }

    }


    @Override
    protected void onCancelled() {

        Log.d("Cancel", "complete");
        if (mLastError != null) {
            Log.d("Error cancelled", mLastError.toString());
        }

    }

}
*/
