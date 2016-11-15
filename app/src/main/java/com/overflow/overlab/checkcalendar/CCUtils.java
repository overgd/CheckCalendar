package com.overflow.overlab.checkcalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by over on 7/13/2016.
 */
public class CCUtils {

    public final String NOWGOAL = "NOWGOAL";
    public final String GOAL = "GOAL";
    public final String ID = "ID";
    public final String SUMMARY = "SUMMARY";
    public final String NULL = "NULL";

    private Context context;
    private SharedPreferences sharedPreferences;
    static FileInputStream fileInputStream;
    static FileOutputStream fileOutputStream;

    public CCUtils(Context context) {
        this.context = context;
    }

    public FileOutputStream fileOutputStream(File file) {

        try {
            fileOutputStream = context.openFileOutput(file.getName(), MODE_PRIVATE);
            return fileOutputStream;
        } catch (FileNotFoundException e) {
            Log.d("Error", "FileOutputStream :" + e.toString());
        }

        return null;
    }

    public FileInputStream fileInputStream(File file) {

        try {
            fileInputStream = context.openFileInput(file.getName());
            return fileInputStream;
        } catch (FileNotFoundException e) {
            Log.d("Error", "FileInputStream :" + e.toString());
        }

        return null;
    }

    public void setGoalList(String id, String summary) {
        sharedPreferences = context.getSharedPreferences(GOAL, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(id, summary);
        editor.apply();
    }

    public String getGoalSummary(String id) {
        sharedPreferences = context.getSharedPreferences(GOAL, MODE_PRIVATE);
        return sharedPreferences.getString(id, NULL);
    }

    public void setCurrentGoal(String id, String summary) {
        sharedPreferences = context.getSharedPreferences(NOWGOAL, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID, id);
        editor.putString(SUMMARY, summary);
        editor.apply();
    }

    public String[] getCurrentGoal() {
        String[] result = new String[2];
        sharedPreferences = context.getSharedPreferences(NOWGOAL, MODE_PRIVATE);
        result[0] = sharedPreferences.getString(ID, NULL);
        result[1] = sharedPreferences.getString(SUMMARY, NULL);

        return result;
    }

    public File goalListFile() {
        return new File(context.getFilesDir(), "goallist");
    }

    public File checkListFile(Calendar calendar) {
        return new File(context.getFilesDir(),
                "check_"
                        +String.valueOf(calendar.get(Calendar.YEAR))
                        +String.valueOf(calendar.get(Calendar.MONTH)));
    }


}
