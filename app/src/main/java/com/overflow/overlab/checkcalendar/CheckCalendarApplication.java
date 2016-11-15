package com.overflow.overlab.checkcalendar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by over on 7/11/2016.
 */
public class CheckCalendarApplication extends Application {

    public final String NOWGOAL = "NOWGOAL";
    public final String GOAL = "GOAL";
    public final String ID = "ID";
    public final String SUMMARY = "SUMMARY";
    public final String NULL = "NULL";

    public GoogleAccountCredential mCredential;
    public com.google.api.services.calendar.Calendar mCalendarService;

    public SharedPreferences sharedPreferences;
    private Activity mainActivity;

    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setGoalList(String id, String summary) {
        sharedPreferences = getSharedPreferences(GOAL, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(id, summary);
        editor.apply();
    }

    public String getGoalSummary(String id) {
        sharedPreferences = getSharedPreferences(GOAL, MODE_PRIVATE);
        return sharedPreferences.getString(id, NULL);
    }

    public void setCurrentGoal(String id, String summary) {
        sharedPreferences = getSharedPreferences(NOWGOAL, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID, id);
        editor.putString(SUMMARY, summary);
        editor.apply();
    }

    public String[] getCurrentGoal() {
        String[] result = new String[2];
        sharedPreferences = getSharedPreferences(NOWGOAL, MODE_PRIVATE);
        result[0] = sharedPreferences.getString(ID, NULL);
        result[1] = sharedPreferences.getString(SUMMARY, NULL);

        return result;
    }

    public File calendarDataFile(Calendar calendar) {

        String eventsYearMonth = null;
        eventsYearMonth = String.format("%s_%s", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        return new File(getApplicationContext().getFilesDir(), "calendar_" + eventsYearMonth);

    }

    public File goalDataFile(String goalSubject) {

        String goalDataSubject = null;
        goalDataSubject = String.format("%s", goalSubject);
        return new File(getApplicationContext().getFilesDir(), "goaldata_" + goalDataSubject);

    }

    /*public FileOutputStream fileOutputStream(File file) {

        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = openFileOutput(file.getName(), MODE_PRIVATE);
            return fileOutputStream;
        } catch (FileNotFoundException e) {
            Log.d("Error", "FileOutputStream :" + e.toString());
        }

        return null;
    }

    public FileInputStream fileInputStream(File file) {

        FileInputStream fileInputStream;

        try {
            fileInputStream = openFileInput(file.getName());
            return fileInputStream;
        } catch (FileNotFoundException e) {
            Log.d("Error", "FileInputStream :" + e.toString());
        }

        return null;
    }*/

    public boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public GoogleAccountCredential setGoogleAccountCredential() {

        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        return mCredential;
    }

    public com.google.api.services.calendar.Calendar setCalendarService(GoogleAccountCredential credential) {

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mCalendarService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("CheckCalendar")
                .build();

        return mCalendarService;
    }

}
