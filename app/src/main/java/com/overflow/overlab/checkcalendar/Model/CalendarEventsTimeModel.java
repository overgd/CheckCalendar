package com.overflow.overlab.checkcalendar.Model;

import com.google.api.client.util.DateTime;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarEventsTimeModel {

    @SerializedName("date")
    @Expose
    private DateTime date;

    @SerializedName("dateTime")
    @Expose
    private DateTime dateTime;

    @SerializedName("timeZone")
    @Expose
    private String timeZone;

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
