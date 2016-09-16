package com.overflow.overlab.checkcalendar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by over on 7/11/2016.
 */
public class CheckCalendarApplication extends Application {

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

    public File calendarDataFile(Calendar calendar) {

        String eventsYearMonth = null;
        eventsYearMonth = String.format("%s_%s", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        return new File(getApplicationContext().getFilesDir(), "calendar_" + eventsYearMonth);

    }

    public File goalDataFile(String goalSubject) {

        String goalDataSubject = null;
        goalDataSubject = String.format("%s", goalSubject);
        return new File(getApplicationContext().getFilesDir(),
                "goaldata_" + goalDataSubject);

    }

    public File goalListFile() {
        return new File(getApplicationContext().getFilesDir(),
                "goallist");
    }

    public FileOutputStream fileOutputStream(File file) {

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
    }

    public boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
