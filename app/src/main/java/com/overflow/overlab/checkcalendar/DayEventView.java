package com.overflow.overlab.checkcalendar;

import android.content.Context;
import android.widget.TextView;

import com.google.gson.Gson;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsModel;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;

/**
 * Created by over on 8/19/2016.
 */
public class DayEventView {

    private CheckCalendarApplication application;
    private Context context;
    private Calendar selectedCalendar;
    private File loadFile;
    private String loadGsonString;
    private CalendarEventsModel selectedEventsModel;

    public DayEventView(long longTime, Context context) {

        this.context = context;
        application = (CheckCalendarApplication) context.getApplicationContext();

        selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTimeInMillis(longTime);

        loadFile = application.calendarDataFile(selectedCalendar);
        FileInputStream fileInputStream = application.fileInputStream(loadFile );

        try {
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            loadGsonString = new String(buffer);
        } catch (Exception e) {

        }

        selectedEventsModel = new Gson().fromJson(loadGsonString, CalendarEventsModel.class);

    }

    public TextView makeEventView() {

        TextView eventView;

        eventView = new TextView(context);

        return eventView;
    }

}
